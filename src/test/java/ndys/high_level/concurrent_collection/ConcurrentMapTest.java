package ndys.high_level.concurrent_collection;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentMapTest {

    @Test
    void concurrentMap() throws InterruptedException {
        final var countDown = new CountDownLatch(100);
        final var map = new ConcurrentHashMap<Integer, String>();
        final var executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            final var index = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(1_000L);
                    map.putIfAbsent(index, "Data-" + index);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDown.countDown();
                }
            });
        }

        executor.execute(() -> {
            try {
                countDown.await();
                map.forEach((integer, s) -> System.out.println(integer + ": " + s));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void testCollection() {
        // Convert collection to a synchronized list
        List<String> list = List.of("Hendy", "Saputra");
        List<String> synchronizedList = Collections.synchronizedList(list);
    }
}
