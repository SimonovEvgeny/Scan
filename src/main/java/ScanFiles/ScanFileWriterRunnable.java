package ScanFiles;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @param <T> - тип объектов для записи
 */

public class ScanFileWriterRunnable<T> implements Runnable {

    private List<T> listToWriteToFile;
    private Path pathResultFile;
    private boolean appendToFile;

    public ScanFileWriterRunnable(List<T> listToWriteToFile, Path pathResultFile, boolean appendToFile) {
        if (listToWriteToFile == null || pathResultFile == null) throw new NullPointerException();

        this.pathResultFile = pathResultFile;
        this.listToWriteToFile = listToWriteToFile;
        this.appendToFile = appendToFile;
    }

    /**
     * Метод записывает в файл по пути pathResultFile список listToWriteToFile<T>
     */
    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(pathResultFile.toFile(), appendToFile);
             OutputStreamWriter dos = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
             PrintWriter pw = new PrintWriter(dos)) {
            for (T s : listToWriteToFile) {
                if (Objects.nonNull(s)) {
                    pw.write(s.toString());
                }
            }
        } catch (IOException er) {
            er.printStackTrace();
            System.exit(1);//Выход из программы в случае получения некоррректного аргумента
        }
    }
}
