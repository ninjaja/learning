package task7tryWithResources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Demo of try-with-resources statement.
 */
public class TryWithResourcesTest {

    public static void main(String[] args) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("resources/task7.txt"))) {
            while ((line = br.readLine()) != null) {
                System.out.println("Line: " + line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
