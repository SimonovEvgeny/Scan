package Scana;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

public class ScanFileWriter<T> implements Runnable {

    private List<T> list;
    private Path path ;

    ScanFileWriter(List<T> list, Path path) {
        this.path = path;
        this.list = list;
    }

    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(path.toFile(), true);
             OutputStreamWriter dos = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
             PrintWriter pw = new PrintWriter(dos)) {
            for (T s : list) {
                pw.write(s.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
