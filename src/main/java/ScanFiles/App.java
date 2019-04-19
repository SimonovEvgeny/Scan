package ScanFiles;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class App {

    private static Logger logger = MyLogger.getInstance(App.class);
    private static final Path resultFilePath = Paths.get(new File("").getAbsolutePath() + "\\src\\main\\result\\result.txt");

    /**
     * main метод программы
     * @param args - пути директорий
     */
    public static void main(String[] args) {

        startBackgroundConsoleOutput();

        if (args.length != 0) {
            /*
            Как заявлено в документации, коллекция работает быстрее
             чем LinkedList, если используется как FIFO
             */
            ArrayDeque<Path> inputPaths = new ArrayDeque<>();

            for(String pathFile: parseInputArguments(args)){

                try{
                    Path path = Paths.get(pathFile);
                    inputPaths.add(path);
                }catch(InvalidPathException er){
                    logger.log(Level.SEVERE,"",er);
                    System.exit(1);//Выход из программы в случае получения некоррректного аргумента
                }
            }
            //Пул потоков для чтения данных, кол-во потоков равно количеству входных параметров
            ExecutorService executorService = Executors.newFixedThreadPool(inputPaths.size());

            List<Future<List<FileStateObject>>> listsFutures = new ArrayList<>();
            while (!inputPaths.isEmpty()) {
                Future<List<FileStateObject>> future = executorService.submit(new FileVisitorCallable(inputPaths.poll(),
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

    /**
     *Метод запускает daemon-поток консольного вывода "." каждые 6 секунд и "|" каждую минуту
     */
     static void startBackgroundConsoleOutput(){
        ExecutorService executorSingleDaemonThread = Executors.newSingleThreadExecutor((runnable)->{
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executorSingleDaemonThread.execute(new BackgroundConsoleOutput());
    }
    /*
    Парсер агрументов командной строки ориентированный на ключ "-", для исключения из вывода файлов типа "Thumbs.db" или
    других типов файлов следует добавить логическое условие в метод visitFile(Path path, BasicFileAttributes attr)
    класса FileVisitorImpl
     */
    /**
     *  Метод проверяет наличие ключа "-" среди параметров, в случае наличия -
     *  удаляет все необходимые пути
     * @param arr исходные параметры
     * @return List<String> Полученный в ходе проверки список параметров
     */
    static List<String> parseInputArguments(String[] arr){
        boolean doInputArgsHaveMinus = false;

        List<String> originalArgs = new ArrayList<>();
        List<String> argsToDelete = new ArrayList<>();
        /*Если среди аргументов есть ключ "-" заполнить два списка: 1- от начала массива до ключа,
        2 - от ключа до конца массива*/
        for (String s : arr) {
            if (!doInputArgsHaveMinus) originalArgs.add(s);
            else argsToDelete.add(s);

            if ("-".equals(s)) {
                doInputArgsHaveMinus = true;
            }
        }
        //Если среди аргументов имеется "-" - удалить из списка все  пути после "-"
        if(doInputArgsHaveMinus) {
            for (String deleteArgument : argsToDelete) {
                if (!originalArgs.remove(deleteArgument)) {
                    logger.log(Level.SEVERE, "", new IllegalArgumentException());
                    System.exit(1);
                }
            }
        }
        return originalArgs;
    }
}
