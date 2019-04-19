package ScanFiles;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 */
public class ScanFileWriter<T> implements Runnable {


    private List<T> listToWriteToFile;
    private Path pathResultFile;

    ScanFileWriter( List<T> listToWriteToFile, Path pathResultFile) {
        if(listToWriteToFile==null||pathResultFile==null)throw new IllegalArgumentException();
        this.pathResultFile = pathResultFile;
        this.listToWriteToFile = listToWriteToFile;
    }

    /**
     * Метод записывает в файл по пути pathResultFile список listToWriteToFile
     */
    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(pathResultFile.toFile(), true);
             OutputStreamWriter dos = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
             PrintWriter pw = new PrintWriter(dos)) {
            for (T s : listToWriteToFile) {
                if (Objects.nonNull(s)) {
                    pw.write(s.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
