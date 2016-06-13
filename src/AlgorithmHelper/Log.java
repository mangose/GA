package AlgorithmHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Sven on 30.05.2016.
 */
public class Log {

    private static String mLog = "";

    public static void addtext(String text) {
        mLog = mLog + text;
    }

    public static void export() throws IOException {
        Files.write(Paths.get("C:/Users/Sven/Desktop/Logexport.csv"), mLog.getBytes());
    }

}
