package Scana;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

class MyLogger {
private static File file = new File(new File("").getAbsolutePath() + "\\src\\main\\log\\scanLog.log");
private static FileHandler fh;

static {
    try {
        fh = new FileHandler(file.getCanonicalPath());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private MyLogger(){
    }

    static java.util.logging.Logger getInstance(Class clas){

        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clas.getName());

        try {

            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return logger;
    }


}
