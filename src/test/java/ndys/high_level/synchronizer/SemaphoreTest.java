package ndys.high_level.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {

    @Test
    void test() throws InterruptedException {
        // Restrict concurrent execution to a single thread at a time
        final var semaphore = new Semaphore(1);
        final ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                try {
                    semaphore.acquire();
                    Thread.sleep(1_000L);
                    System.out.println("Finish");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                }
            });
        }

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
