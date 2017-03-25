import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by petro on 23-Mar-17.
 */
public class WordCounter {

    private static StringBuilder sb = new StringBuilder();
   //private static String[] words;
    private static File filename = new File("C:\\Users\\petro\\IdeaProjects\\WordCount\\src\\books1-14.txt"); // file to read from
    private static String[] textBlocks = new String[4];
    private static HashMap<String, Integer> mainHashMap = new HashMap<>();

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
        for(OurThread thread : threads) {
            for (Map.Entry<String, Integer> e : thread.wordCountHashMap.entrySet())
                mainHashMap.merge(e.getKey(), e.getValue(), Integer::sum);
        }
        //System.out.println("MainHashMap: " + mainHashMap.toString());
        executionTime = System.nanoTime() - wordCountStartTime;
        System.out.println("Counting words time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));

        wordCounter.sortByOccurrences(mainHashMap);
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

    private void divideString() {
        String text = sb.toString();
        textBlocks[0] = text.substring(0, (int)(text.length() * 0.25));
        textBlocks[1] = text.substring((int)(text.length() * 0.25), (int)(text.length() * 0.5));
        textBlocks[2] = text.substring((int)(text.length() * 0.5), (int)(text.length() * 0.75));
        textBlocks[3] = text.substring((int)(text.length() * 0.75), text.length());
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
        public OurThread(String text)
        {
            this.text=text;
        }
        private String[] extractOnlyWords(){
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