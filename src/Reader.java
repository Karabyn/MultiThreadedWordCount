import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by petro on 23-Mar-17.
 */
public class Reader {

    public StringBuilder sb = new StringBuilder();

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Reader reader = new Reader();
        reader.readFile();
        long executionTime = System.nanoTime() - startTime;
        System.out.println("Reading time: " + String.format("%d s %d ms", TimeUnit.NANOSECONDS.toSeconds(executionTime),
                TimeUnit.NANOSECONDS.toMillis(executionTime) - TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(executionTime))));
        System.out.println("Reading time: " + String.format("%d ms", TimeUnit.NANOSECONDS.toMillis(executionTime)));
        reader.extractOnlyWords();
    }

    public void readFile() {
        File filename = new File("C:\\Users\\petro\\IdeaProjects\\WordCount\\src\\books1-14.txt");
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

    public void extractOnlyWords(){
        //String str = sb.toString().replaceAll("[!?,.’‘-”-*:)(]", " ").toLowerCase();
        //String[] words = str.split("\\s+");

        String[] words = sb.toString().replaceAll("[\\W]", " ").toLowerCase().split("\\s++");
        //String[] words = str.split("\\s++");

        String[] sample = Arrays.copyOfRange(words, 0, 100);
        System.out.println(Arrays.toString(sample));
        System.out.println(words.length);
        wordCount(words);
    }

    public void wordCount(String[] words) {
        HashMap<String, Integer> wordCount = new HashMap<>();
        for(String word : words) {
            if(!wordCount.containsKey(word)){
                wordCount.put(word, 1);
            }
            else {
                wordCount.put(word, wordCount.get(word) + 1);
            }
        }
        System.out.println(wordCount.toString());
    }




}
