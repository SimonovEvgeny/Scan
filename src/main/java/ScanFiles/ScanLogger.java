package ScanFiles;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * Класс - настройка логирования в файл
 */
class ScanLogger {
    private static File logFile = new File(new File("").getAbsolutePath() + "\\src\\main\\log\\scanLog.log");
    private static FileHandler fh;

    static {
        try {
            fh = new FileHandler(logFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ScanLogger() {
    }

    /**
     * @param clas - имя класса для идентификации лога в файле
     * @return Logger настроенный на запись в файл logFile
     */
    static Logger getLogger(Class clas) {
        if (clas == null) throw new NullPointerException();
        Logger logger = Logger.getLogger(clas.getName());

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
