package ScanFiles;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static Logger logger = ScanLogger.getLogger(Main.class);
    private static Path resultFilePath = Paths.get(new File("").getAbsolutePath() + "\\src\\main\\result\\result.txt");

    static void setResultFilePath(Path resultFilePath) {
        Main.resultFilePath = resultFilePath;
    }

    /**
     * main метод программы
     *
     * @param args - пути директорий для поиска файлов
     */
    public static void main(String[] args) {

        startBackgroundConsoleOutput();

        if (args.length != 0) {
            /*
            Как заявлено в документации, коллекция работает быстрее
             чем LinkedList, если используется как FIFO
             */
            ArrayDeque<Path> inputPaths = new ArrayDeque<>();

            for (String pathFile : parseInputArguments(args)) {
                Path path = Paths.get(pathFile);
                inputPaths.add(path);
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
                    singleExecutor.execute(new ScanFileWriterRunnable<>(future.get(), resultFilePath, true));
                } catch (InterruptedException | ExecutionException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
            executorService.shutdown();
            singleExecutor.shutdown();
        } else {
            System.out.println("No options to run the application");
        }
    }

    /**
     * Метод запускает daemon-поток консольного вывода "." каждые 6 секунд и "|" каждую минуту
     */
    private static void startBackgroundConsoleOutput() {
        ExecutorService executorSingleDaemonThread = Executors.newSingleThreadExecutor((runnable) -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executorSingleDaemonThread.execute(new BackgroundConsoleOutputRunnable());
    }
    /*
    Парсер агрументов командной строки ориентированный на ключ "-", для исключения из вывода файлов типа "Thumbs.db" или
    других типов файлов следует добавить логическое условие в метод visitFile(Path path, BasicFileAttributes attr)
    класса FileVisitorImpl
     */

    /**
     * Метод проверяет наличие ключа "-" среди параметров, в случае наличия -
     * удаляет все необходимые пути
     *
     * @param inputArgs исходные параметры
     * @return List<String> Полученный в ходе проверки список параметров
     */
    static List<String> parseInputArguments(String[] inputArgs) {
        boolean doInputArgsHaveMinus = false;

        List<String> originalArgs = new ArrayList<>();
        List<String> argsToDelete = new ArrayList<>();
        /*Если среди аргументов есть ключ "-" заполнить два списка: 1- от начала массива до ключа,
        2 - от ключа до конца массива*/
        for (String s : inputArgs) {
            if (!doInputArgsHaveMinus) originalArgs.add(s);
            else argsToDelete.add(s);

            if ("-".equals(s)) {
                doInputArgsHaveMinus = true;
            }
        }
        //Если среди аргументов имеется "-" - удалить из списка все  пути после "-"
        if (doInputArgsHaveMinus) {
            for (String deleteArgument : argsToDelete) {
                if (!originalArgs.remove(deleteArgument)) {
                    throw new IllegalArgumentException();
                }
            }
            originalArgs.remove("-");
        }
        return originalArgs;
    }
}
