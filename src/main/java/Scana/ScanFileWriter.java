package Scana;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ScanFileWriter implements Runnable {

    private List<String> list;
    private Path path = Paths.get(new File("").getAbsolutePath()+ "\\src\\main\\result\\result.txt");
    private FileOutputStream fos = new FileOutputStream(path.toFile(),true);
    private DataOutputStream dos = new DataOutputStream(fos);

    ScanFileWriter(List<String> list ) throws FileNotFoundException {

        this.list=list;
    }

    @Override
    public void run() {
        try{
            for(String s : list){
                dos.writeUTF(s);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
