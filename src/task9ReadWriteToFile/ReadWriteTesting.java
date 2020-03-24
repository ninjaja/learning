package task9ReadWriteToFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 * Demo of multi-thread reading from different files and writing (appending) them in one file.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ReadWriteTesting {

    private static final String INPUT_FOLDER_PATH = "resources/task9/input/";
    private static final String OUTPUT_FILE_PATH = "resources/task9/output/task9output.txt";

    private static String[] files = new File(INPUT_FOLDER_PATH).list();
    static File outputFile;

    public static void main(String[] args) {
        outputFile = new File(OUTPUT_FILE_PATH);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        runReadWriters();
    }

    public static void runReadWriters() {
        for (String filePath : files) {
            new Thread(new ReadWriter((INPUT_FOLDER_PATH + filePath), outputFile)).start();
        }
    }

    static class ReadWriter implements Runnable {

        private final String filePath;
        private final File outputFile;

        public ReadWriter(String filePath, File outputFile) {
            this.filePath = filePath;
            this.outputFile = outputFile;
        }

        /**
         * Thread-safe method to write input text to output file
         */
        @Override
        public void run() {
            try {
                synchronized (outputFile) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                        FileWriter fileWriter = new FileWriter(outputFile, true);
                        try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                            String line;
                            while (Objects.nonNull(line = reader.readLine())) {
                                writer.append(line);
                                writer.newLine();
                            }
                            writer.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
