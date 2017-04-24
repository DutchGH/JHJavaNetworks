package networks.cw2;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by jacob on 24/04/17.
 */
public class Log {
    public Logger logger;
    FileHandler fh;

    public Log(String fileName) throws SecurityException, IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }

        fh = new FileHandler(fileName, true);
        logger = Logger.getLogger("serverLogger");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
}
