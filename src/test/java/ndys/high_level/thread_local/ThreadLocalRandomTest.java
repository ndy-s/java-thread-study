package ndys.high_level.thread_local;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadLocalRandomTest {

    @Test
    void test() throws InterruptedException {
        final var executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(1_000L);
                    var number = ThreadLocalRandom.current().nextInt();
                    System.out.println("Number: " + number);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void stream() throws InterruptedException {
        final var executor = Executors.newFixedThreadPool(100);

        executor.execute(() -> {
            ThreadLocalRandom.current().ints(1000, 0, 1000).forEach(System.out::println);
        });

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
