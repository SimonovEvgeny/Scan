package Scana;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileVisitorCallable implements Callable<List<String>> {

    private Path path;
    private FileVisitorImpl<Path> fileVisitor;
    private Logger logger = MyLogger.getInstance(FileVisitorRunnuble.class);



    FileVisitorCallable(Path path, FileVisitorImpl<Path> fileVisitor) {
        this.fileVisitor = fileVisitor;
        this.path = path;
    }


    @Override
    public List<String> call() throws Exception {
        try {
            Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                    Integer.MAX_VALUE, fileVisitor);

        } catch (IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
        return fileVisitor.getList();
    }
}
