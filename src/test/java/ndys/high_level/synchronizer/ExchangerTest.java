package ndys.high_level.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {

    @Test
    void test() throws InterruptedException {
        final var exchanger = new Exchanger<String>();
        final var executor = Executors.newFixedThreadPool(10);

        executor.execute(() -> {
            try {
                Thread.sleep(1_000L);
                var value = exchanger.exchange("First");
                System.out.println("Thread 1: Receive " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.execute(() -> {
            try {
                Thread.sleep(2_000L);
                var value = exchanger.exchange("Second");
                System.out.println("Thread 2: Receive " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
