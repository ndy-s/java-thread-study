package ndys.high_level;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutureTest {

    @Test
    void testFuture() throws ExecutionException, InterruptedException {
        var executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            Thread.sleep(5_000L);
            return "Hello";
        };

        Future<String> future = executor.submit(callable);
        System.out.println("Future done.");

        while (!future.isDone()) {
            System.out.println("Waiting future...");
            Thread.sleep(1_000L);
        }

        String value = future.get();
        System.out.println(value);
    }

    @Test
    void testFutureCancel() throws ExecutionException, InterruptedException {
        var executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            Thread.sleep(5_000L);
            return "Hello";
        };

        Future<String> future = executor.submit(callable);
        System.out.println("Future done.");

        Thread.sleep(2_000L);
        future.cancel(true);

        System.out.println(future.isCancelled());
        String value = future.get();
        System.out.println(value);
    }

    @Test
    void testInvokeAll() throws InterruptedException, ExecutionException {
        var executor = Executors.newFixedThreadPool(10);

        List<Callable<String>> callables = IntStream.range(1, 11).mapToObj(value -> (Callable<String>) () -> {
            Thread.sleep(value * 500L);
            return String.valueOf(value);
        }).collect(Collectors.toList());

        var futures = executor.invokeAll(callables);

        for (Future<String> stringFuture : futures) {
            System.out.println(stringFuture.get());
        }
    }

    @Test
    void testInvokeAny() throws InterruptedException, ExecutionException {
        var executor = Executors.newFixedThreadPool(10);

        List<Callable<String>> callables = IntStream.range(1, 11).mapToObj(value -> (Callable<String>) () -> {
            Thread.sleep(value * 500L);
            return String.valueOf(value);
        }).collect(Collectors.toList());

        var value = executor.invokeAny(callables);

        System.out.println(value);
    }
}
