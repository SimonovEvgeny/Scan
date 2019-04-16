package Scana;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;



/**
 * Hello world!
 *
 */
public class App

{
    public static void main( String[] args )  {


        Thread outputConsoleDaemon = new Thread(new BackgroundConsoleOutput());
        outputConsoleDaemon.setDaemon(true);
        outputConsoleDaemon.start();

        if(args.length!=0) {
            List<Path> inputPaths = new ArrayList<>();

            Arrays.stream(args).map(str -> Paths.get(str)).forEach(inputPaths::add);

            BlockingQueue<Path> paths = new ArrayBlockingQueue<>(inputPaths.size());
            paths.addAll(inputPaths);

            ExecutorService executorService = Executors.newFixedThreadPool(inputPaths.size());


            while (!paths.isEmpty()) {
                executorService.execute(new FileVisitorRunnuble(paths.poll(), new FileVisitorImpl<>()));
            }

            executorService.shutdown();


//        File file = new File("");

//            FileVisitorImpl pf = new FileVisitorImpl();
//            try {
//                Files.walkFileTree(file.toPath(),pf);
//            }catch (FileNotFoundException e){
//                System.err.println(e.getStackTrace());
//            }catch
//             (IOException e) {
//            }

        }
    }


}
