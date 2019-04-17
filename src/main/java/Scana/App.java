package Scana;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Hello world!
 */
public class App {



    static {
        Thread outputConsoleDaemon = new Thread(new BackgroundConsoleOutput());
        outputConsoleDaemon.setDaemon(true);
        outputConsoleDaemon.start();
    }

    public static void main(String[] args) {

        Logger logger = MyLogger.getInstance(App.class);


        if (args.length != 0) {

            List<Path> inputPaths = new ArrayList<>();

            Arrays.stream(args).map(str -> Paths.get(str)).forEach(inputPaths::add);

            //parse input

            BlockingQueue<Path> pathsBlockingQueue = new ArrayBlockingQueue<>(inputPaths.size(), false, inputPaths);

            ExecutorService executorService = Executors.newFixedThreadPool(inputPaths.size());//потоки чтения
            ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

            List<Future<List<String>>> listsFutures = new ArrayList<>();
            while (!pathsBlockingQueue.isEmpty()) {
                Future<List<String>> future = executorService.submit(new FileVisitorCallable(pathsBlockingQueue.poll(),
                        new FileVisitorImpl<>()));
                listsFutures.add(future);
            }

            for (Future<List<String>> future : listsFutures) {
                try {
                    singleExecutor.execute(new ScanFileWriter(future.get()));

                } catch (InterruptedException | ExecutionException | FileNotFoundException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
            executorService.shutdown();
            singleExecutor.shutdown();


        }
    }


}
