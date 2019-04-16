package Scana;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;


import static java.nio.file.FileVisitResult.*;

public class FileVisitorImpl<Path>
        extends SimpleFileVisitor<Path> {

   private final Logger myLogger = MyLogger.getInstance(FileVisitorImpl.class);
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {


        System.out.println("[");
        System.out.println("file= \\" + path);
        System.out.println("date= " + attr.creationTime().toInstant());
        System.out.print("size= " + attr.size() + "]");
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        myLogger.log(Level.SEVERE, "ошибка доступа к файлу: " + file, exc);
        return CONTINUE;
    }
}
