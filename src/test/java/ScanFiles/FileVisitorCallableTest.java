package ScanFiles;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class FileVisitorCallableTest {

    @Test(expected = NullPointerException.class)
    public void testNullArgumentsInConstructThrowsNPE() {
        new FileVisitorCallable(null, null);
    }

    @Test
    public void testCorrectResultCall() throws IOException {
        File file = new File(new File("").getAbsolutePath()+"\\src\\test\\result\\testFileWriterResult.txt");
        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        List<String> expectedStrings = Arrays.asList(
                "file= " + file.getAbsolutePath() ,
                "date= " + attr.lastModifiedTime().toString().substring(0,10).replaceAll("-","."),
                "size= " + attr.size());

        FileVisitorCallable fileVisitorCallable = new FileVisitorCallable(Paths.get(new File("").getAbsolutePath()+"\\src\\test\\result\\"),
                new FileVisitorImpl<>());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<FileStateObject>> future = executorService.submit(fileVisitorCallable);
        List<String> actualStrings = null;
        try {
           actualStrings = Arrays.asList(future.get()
                    .get(0)
                    .toString()
                    .split("\n"));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(expectedStrings.get(0),actualStrings.get(1));
        Assert.assertEquals(expectedStrings.get(1),actualStrings.get(2));

    }
}