package Scana;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {

    private static Logger logger = MyLogger.getInstance(App.class);
    private static final Path resultFilePath = Paths.get(new File("").getAbsolutePath() + "\\src\\main\\result\\result.txt");


    public static void main(String[] args) {

        startBackgroundConsoleOutput();

        if (args.length != 0) {

            List<Path> inputPaths = new ArrayList<>();
            for(String path: parseInputArguments(args)){
                inputPaths.add(Paths.get(path));
            }


            BlockingQueue<Path> pathsBlockingQueue = new ArrayBlockingQueue<>(inputPaths.size(), false, inputPaths);
            //Пул потоков для чтения данных кол-во потоков равно количеству входных параметров
            ExecutorService executorService = Executors.newFixedThreadPool(inputPaths.size());//потоки чтения

            List<Future<List<FileStateObject>>> listsFutures = new ArrayList<>();//лист future

            while (!pathsBlockingQueue.isEmpty()) {
                Future<List<FileStateObject>> future = executorService.submit(new FileVisitorCallable(pathsBlockingQueue.poll(),
                        new FileVisitorImpl<>()));
                listsFutures.add(future);
            }

            //Один поток для записи данных в файл для сохранения порядка записей
            ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

            for (Future<List<FileStateObject>> future : listsFutures) {
                try {
                    singleExecutor.execute(new ScanFileWriter<>(future.get(),resultFilePath));
                } catch (InterruptedException | ExecutionException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
            executorService.shutdown();
            singleExecutor.shutdown();
        }
    }
    //Метод запускающий поток консольного вывода "." и "|"
    private static void startBackgroundConsoleOutput(){
        ExecutorService singleDaemonThread = Executors.newSingleThreadExecutor((runnable)->{
            Thread t = Executors.defaultThreadFactory().newThread(runnable);
            t.setDaemon(true);
            return t;
        });
        singleDaemonThread.execute(new BackgroundConsoleOutput());
    }

    //парсер командной строки ориентированный на ключ "-", для исключения из вывода файлов типа "Thumbs.db" или
    //других типов файлов следует добавить логическое условие в метод visitFile(Path path, BasicFileAttributes attr)
    //класса FileVisitorImpl
    private static List<String> parseInputArguments(String[] arr){
        boolean inputArgsHaveMinus = false;
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        for (String s : arr) {

            if (!inputArgsHaveMinus) list1.add(s);
            else list2.add(s);

            if ("-".equals(s)) {
                inputArgsHaveMinus = true;
            }
        }
        for(String deleteLine: list2){
            if(!list1.remove(deleteLine))throw  new IllegalArgumentException("элемента в списке нет");
        }
        return list1;
    }


}
