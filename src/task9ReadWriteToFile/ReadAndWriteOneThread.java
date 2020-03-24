package task9ReadWriteToFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Demo of single-thread reading from different files and writing (appending) them in one file.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ReadAndWriteOneThread {

    private static final String OUTPUT_FILE_PATH = "resources/task9/output/task9outputOneThread.txt";
    private static final String INPUT_FOLDER_PATH = "resources/task9/input/";
    private static String[] files = new File(INPUT_FOLDER_PATH).list();
    String filePath;

    public ReadAndWriteOneThread(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        for (String filePath : files) {
            readAndWriteFromFile(INPUT_FOLDER_PATH + filePath);
        }
    }

    public static void readAndWriteFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            File outputFile = new File(OUTPUT_FILE_PATH);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(outputFile, true);
            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                String line;
                while (Objects.nonNull(line = reader.readLine())) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
