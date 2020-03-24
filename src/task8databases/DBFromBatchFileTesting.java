package task8databases;

import java.io.File;
import java.io.IOException;

/**
 * Demo of creating database from batch file
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */

public class DBFromBatchFileTesting {

    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime.getRuntime().exec("cmd /c database.bat", null, new File("resources"));
    }
}
