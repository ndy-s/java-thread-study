package ndys.high_level;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    @Test
    void parallel() {
        Stream<Integer> stream = IntStream.range(0, 1000).boxed();
        stream.parallel().forEach(integer -> {
            System.out.println(Thread.currentThread().getName() + ": " + integer);
        });
    }

    @Test
    void customPool() {
        // Parallelism
        ForkJoinPool pool = new ForkJoinPool(5);

        ForkJoinTask<?> task = pool.submit(() -> {
            Stream<Integer> stream = IntStream.range(0, 1000).boxed();
            stream.parallel().forEach(integer -> {
                System.out.println(Thread.currentThread().getName() + ": " + integer);
            });
        });

        task.join();
    }
}
