package ndys.high_level;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    @Test
    void create() {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;

        var queue = new ArrayBlockingQueue<Runnable>(100);

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue);
    }

    @Test
    void executeRunnable() throws InterruptedException {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;

        var queue = new ArrayBlockingQueue<Runnable>(100);

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue);
        executor.execute(() -> {
            try {
                Thread.sleep(5_000L);
                System.out.println("Hello from ThreadPool: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(6_000L);
    }

    @Test
    void shutdown() throws InterruptedException {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;

        var queue = new ArrayBlockingQueue<Runnable>(1000);

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue);

        for (int i = 0; i < 1000; i++) {
            final var task = i;
            Runnable runnable = () -> {
                try {
                    Thread.sleep(1_000L);
                    System.out.println("Task " + task + " from ThreadPool: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            executor.execute(runnable);
        }

        // Finishes all tasks, then stops the executor
        executor.shutdown();

        // Stops the executor now, returns unstarted tasks
        // executor.shutdownNow();

        // Waits up to 1 day for executor to finish, returns true if done, false if timed out
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    // To limit queue, so its not memory overflow
    public static class LogRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Task: " + r + " is rejected");
        }
    }

    @Test
    void rejected() throws InterruptedException {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;

        var queue = new ArrayBlockingQueue<Runnable>(10);
        var rejectedHandler = new LogRejectedExecutionHandler();

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue, rejectedHandler);

        for (int i = 0; i < 1000; i++) {
            final var task = i;

            Runnable runnable = () -> {
                try {
                    Thread.sleep(1_000L);
                    System.out.println("Task " + task + " from ThreadPool: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };

            executor.execute(runnable);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

}
