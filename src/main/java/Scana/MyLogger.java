package Scana;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

class MyLogger {

    private MyLogger(){
    }

    static java.util.logging.Logger getInstance(Class clas){

        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clas.getName());

        try {
            FileHandler fh = new FileHandler(new File("").getAbsolutePath() + "\\src\\main\\log\\scanLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        return logger;
    }


}
