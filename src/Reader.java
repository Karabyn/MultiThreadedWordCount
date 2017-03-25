import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by petro on 23-Mar-17.
 */
public class Reader {
    private static StringBuilder sb = new StringBuilder();
   // private static String[] words;

    private static File filename = new File("C:\\Users\\naliv\\IdeaProjects\\WordCount\\src\\test.txt"); // file to read from
    private static String[] textBlocks = new String[4];
    private static HashMap<String, Integer> mainHashMap = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Reader reader = new Reader();

        // reading
        long readingStartTime = System.nanoTime();
        reader.readFile(filename);
        long executionTime = System.nanoTime() - readingStartTime;
        System.out.println("Reading time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));
        reader.divideString();
        OurThread thread1 = new OurThread(textBlocks[0]);
        OurThread thread2 = new OurThread(textBlocks[1]);
        OurThread thread3 = new OurThread(textBlocks[2]);
        OurThread thread4 = new OurThread(textBlocks[3]);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        mainHashMap.putAll(thread1.wordCountHashMap);
        for (Map.Entry<String, Integer> e : thread2.wordCountHashMap.entrySet())
            mainHashMap.merge(e.getKey(), e.getValue(), Integer::sum);

        //System.out.println("Reading time: " + String.format("%d ms", TimeUnit.NANOSECONDS.toMillis(executionTime)));

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
    private void divideString()
    {
        String text = sb.toString();
        textBlocks[0] = text.substring(0, (int)(text.length() * 0.25));
        textBlocks[1] = text.substring((int)(text.length() * 0.25), (int)(text.length() * 0.5));
        textBlocks[2] = text.substring((int)(text.length() * 0.5), (int)(text.length() * 0.75));
        textBlocks[3] = text.substring((int)(text.length() * 0.75), text.length());
        for(int i=0;i<textBlocks.length;i++)
        {
            System.out.println("Block:  ");
            System.out.println(textBlocks[i]);
        }
    }




    private void sortByOccurences(HashMap wordCountHashMap) {
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
    class OurThread extends Thread
    {
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
         //   System.out.println(wordCountHashMap.toString());
 //           System.out.println(numberOfDistinctWords(wordCountHashMap));
        }
        @Override
        public void run() {
        String[] block1 = extractOnlyWords();
        wordCount(block1);

        }
    }