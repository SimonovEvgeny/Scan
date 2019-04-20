package ScanFiles;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainTest {

    @Test(timeout = 500)
    public void testTimeoutWithoutMethodArguments() {
        Main.main(new String[0]);
    }

    @Test
    public void testCorrectArgumentsInMethod() throws IOException {

        File file = new File(new File("").getAbsolutePath() + "\\src\\test\\result\\testMainResult.txt");
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        List<String> expectedStrings = Arrays.asList(
                "file= " + file.getAbsolutePath(),
                "date= " + attr.lastModifiedTime().toString().substring(0, 10).replaceAll("-", "."),
                "size= " + attr.size());

        Main.setResultFilePath(Paths.get(new File("").getAbsolutePath() + "\\src\\test\\result\\testMainResult.txt"));

        Main.main(new String[]{file.getCanonicalPath()});

        List<String> resultList = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(new File("").getAbsolutePath() + "\\src\\test\\result\\testMainResult.txt"))) {
            Thread.sleep(500);
            while (scanner.hasNext()) {
                resultList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(expectedStrings.get(1), resultList.get(2));


    }

    @Test
    public void testCorrectParseInputArguments() {
        String[] originalStrings = new String[]{
                "1", "2", "3", "-", "2", "3"
        };
        String expectedString = "1";

        List<String> actualString = Main.parseInputArguments(originalStrings);

        Assert.assertEquals(expectedString, actualString.get(0));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentsThrowsException() {
        String[] methodArguments = new String[]{
                "1", "2", "3", "-", "2", "3", "4"
        };
        Main.parseInputArguments(methodArguments);
    }
}
