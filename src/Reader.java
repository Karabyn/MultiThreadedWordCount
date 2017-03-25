import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by petro on 23-Mar-17.
 */
public class Reader {

    private static StringBuilder sb = new StringBuilder();
    private static String[] words;
    private static HashMap<String, Integer> wordCountHashMap = new HashMap<>();
    private static File filename = new File("C:\\Users\\petro\\IdeaProjects\\WordCount\\src\\books1-14.txt"); // file to read from

    public static void main(String[] args) {
        Reader reader = new Reader();

        // reading
        long readingStartTime = System.nanoTime();
        reader.readFile(filename);
        long executionTime = System.nanoTime() - readingStartTime;
        System.out.println("Reading time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));
        //System.out.println("Reading time: " + String.format("%d ms", TimeUnit.NANOSECONDS.toMillis(executionTime)));

        // extracting words
        long extractingWordsStartTime = System.nanoTime();
        reader.extractOnlyWords(sb);
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

    private void extractOnlyWords(StringBuilder sb){
        words = sb.toString().replaceAll("[\\W]", " ").toLowerCase().split("\\s++");

        String[] sample = Arrays.copyOfRange(words, 0, 100);
        System.out.println(Arrays.toString(sample));
        //System.out.println(words.length);
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
        System.out.println(wordCountHashMap.toString());
        System.out.println(numberOfDistinctWords(wordCountHashMap));
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
