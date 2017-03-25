import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by petro on 23-Mar-17.
 */

public class WordCounter {

    private static StringBuilder sb = new StringBuilder();
    //private static String[] words;
    private static File filename = new File("C:\\Users\\petro\\IdeaProjects\\WordCount\\src\\books1-14.txt"); // file to read from
    private static HashMap<String, Integer> mainHashMap = new HashMap<>();
    private static int numberOfThreads = 4;
    private static String[] textBlocks = new String[numberOfThreads];

    public static void main(String[] args) throws InterruptedException {
        WordCounter wordCounter = new WordCounter();

        // reading
        long readingStartTime = System.nanoTime();
        wordCounter.readFile(filename);
        long executionTime = System.nanoTime() - readingStartTime;
        System.out.println("Reading time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));

        // extracting and counting words in threads
        long wordCountStartTime = System.nanoTime();
        wordCounter.divideString();

        OurThread[] ourThreads = new OurThread[numberOfThreads];
        for(int i = 0; i < numberOfThreads; i++) {
            ourThreads[i] = new OurThread(textBlocks[i]);
            ourThreads[i].start();
        }
        for(OurThread thread : ourThreads) {
            thread.join();
            for (Map.Entry<String, Integer> e : thread.wordCountHashMap.entrySet())
                mainHashMap.merge(e.getKey(), e.getValue(), Integer::sum);
        }
        executionTime = System.nanoTime() - wordCountStartTime;
        System.out.println("Counting words time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));

        wordCounter.sortByOccurrences(mainHashMap);
        //wordCounter.sortByAlphabet(mainHashMap);

        /*
        OurThread thread1 = new OurThread(textBlocks[0]);
        OurThread thread2 = new OurThread(textBlocks[1]);
        OurThread thread3 = new OurThread(textBlocks[2]);
        OurThread thread4 = new OurThread(textBlocks[3]);
        OurThread threads[] = {thread1, thread2, thread3, thread4};
        for(OurThread thread : threads) {
            thread.start();
        }
        for(OurThread thread : threads) {
            thread.join();
        }

        //wordCounter.sortByAlphabet(thread1.wordCountHashMap);
        //wordCounter.sortByAlphabet(thread2.wordCountHashMap);


        for(OurThread thread : threads) {
            for (Map.Entry<String, Integer> e : thread.wordCountHashMap.entrySet())
                mainHashMap.merge(e.getKey(), e.getValue(), Integer::sum);
        }
        */
        /*
        // extracting words
        long extractingWordsStartTime = System.nanoTime();
        reader.extractOnlyWords();
        executionTime = System.nanoTime() - extractingWordsStartTime;
        System.out.println("Extracting words time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));

        // counting words
        long wordCountStartTime = System.nanoTime();
        reader.wordCount(words);
        executionTime = System.nanoTime() - wordCountStartTime;
        System.out.println("Counting words time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));


        //reader.sortByOccurences(wordCountHashMap);
        //reader.sortByAlphabet(wordCountHashMap);
        */
    }

    private void readFile(File filename) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Divides the input text into equal parts according
     * to the number of threads for further processing.
     */
    private void divideString() {
        String text = sb.toString();
        int textLength = text.length();
        double n = numberOfThreads;
        for(int i = 0; i < numberOfThreads; i ++) {
            textBlocks[i] = text.substring((int)(textLength * (i / n)), (int)(textLength * ((i + 1) / n)));
            //System.out.println(i + "; " + (int)(textLength * (i / n))  + "; " + (int)(textLength * ((i + 1) / n)));
            //System.out.println(textBlocks[i]);
            //System.out.println(text.substring(0, 2));
        }

    }

    private void sortByOccurrences(HashMap wordCountHashMap) {
        List list = new ArrayList(wordCountHashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });
        System.out.println(list);
    }

    private void sortByAlphabet(HashMap wordCountHashMap) {
        List list = new ArrayList(wordCountHashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return a.getKey().compareTo(b.getKey());
            }
        });
        System.out.println(list);
    }

    private int numberOfDistinctWords(HashMap<String, Integer> wordCount) {
        return wordCount.size();
        }

}
    class OurThread extends Thread {

        private String text;
        public HashMap<String,Integer> wordCountHashMap = new HashMap<>();

        public OurThread(String text) {
            this.text=text;
        }

        private String[] extractOnlyWords() {
            return text.replaceAll("[\\W]", " ").toLowerCase().split("\\s++");
        }

        private void wordCount(String[] words) {
            for(String word : words) {
                if(!wordCountHashMap.containsKey(word)){
                    wordCountHashMap.put(word, 1);
                }
                else {
                    wordCountHashMap.put(word, wordCountHashMap.get(word) + 1);
                }
            }
        }

        @Override
        public void run() {
            String[] block = extractOnlyWords();
            wordCount(block);
        }
    }