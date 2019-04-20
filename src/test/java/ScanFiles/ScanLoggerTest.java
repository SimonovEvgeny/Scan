package ScanFiles;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ScanLoggerTest {


    @Test(expected = NullPointerException.class)
    public void testNullArgumentsInConstructThrowsNPE() {
        ScanLogger.getLogger(null);
    }

    @Test
    public void testCorrectWriteLog() throws IOException {

        File logFile = new File(new File("").getAbsolutePath() + "\\src\\test\\log\\scanLog.log");

        Logger logger = ScanLogger.getLogger(ScanLoggerTest.class);
        FileHandler fileHandler = new FileHandler(logFile.getAbsolutePath());
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);

        logger.log(Level.SEVERE, "", new NullPointerException());
        List<String> resultlog = new ArrayList<>();
        try(Scanner scanner = new Scanner(new FileInputStream(new File("").getAbsolutePath() + "\\src\\test\\log\\scanLog.log"))){
            while(scanner.hasNext()){
                resultlog.add(scanner.nextLine());
            }
        }
        String testString = "java.lang.NullPointerException";
        Assert.assertEquals(testString,resultlog.get(2));
    }
}