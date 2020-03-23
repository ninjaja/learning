package task9ReadWriteToFile;

import java.io.*;
import java.util.Objects;

public class ReadAndWriteOneThread {

    private static final String OUTPUT_FILE_PATH = "resources/task9/output/task9outputOneThread.txt";
    private static final String INPUT_FILE_PATH = "resources/task9/input/task9input.txt";

    String filePath;

    public ReadAndWriteOneThread(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        readAndWriteFromFile(INPUT_FILE_PATH);
    }

    public static void readAndWriteFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            File outputFile = new File(OUTPUT_FILE_PATH);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(outputFile.getAbsoluteFile());
            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                String line;
                while (Objects.nonNull(line = reader.readLine())) {
                    writer.write(line);
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
