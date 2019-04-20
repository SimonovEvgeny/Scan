package ScanFiles;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileVisitorCallable implements Callable<List<FileStateObject>> {

    private Path pathFolder;
    private FileVisitorImpl<Path> fileVisitor;
    private Logger logger = ScanLogger.getLogger(FileVisitorCallable.class);

    FileVisitorCallable(Path pathFolder, FileVisitorImpl<Path> fileVisitor) {
        if (pathFolder == null || fileVisitor == null) throw new NullPointerException();
        this.fileVisitor = fileVisitor;
        this.pathFolder = pathFolder;
    }

    /**
     * @return List<FileStateObject>
     */
    @Override
    public List<FileStateObject> call() {
        try {
            Files.walkFileTree(pathFolder, EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                    Integer.MAX_VALUE, fileVisitor);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return fileVisitor.getList();
    }
}
