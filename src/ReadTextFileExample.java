/**
 * Created by petro on 23-Mar-17.
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
/**
 * A simple example program that reads a text file into a String using Files.lines and stream.
 */
public class ReadTextFileExample {
    public static void main(String[] args) throws IOException {
        String contents = Files.lines(Paths.get("C:\\Users\\petro\\IdeaProjects\\WordCount\\src\\books1-14.txt")).collect(Collectors.joining("\n"));
        System.out.println(contents);
    }
}