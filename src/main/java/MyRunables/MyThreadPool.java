package MyRunables;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {
    public static ExecutorService pool = Executors.newFixedThreadPool(1000);

    public static void doWork(Runnable runnable) {
        pool.execute(runnable);
    }
}
