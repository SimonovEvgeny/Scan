package Scana;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileVisitorRunnuble implements java.lang.Runnable {

    private Path path;
    private FileVisitor<Path> fileVisitor;
    private Logger logger = MyLogger.getInstance(FileVisitorRunnuble.class);

    FileVisitorRunnuble(Path path, FileVisitor<Path> fileVisitor) {
        this.fileVisitor = fileVisitor;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                    Integer.MAX_VALUE, fileVisitor);

        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }
}
