package ndys.high_level;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.*;

public class CompleteableFutureTest {

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    private Random random = new Random();

    public CompletableFuture<String> getValue() {
        CompletableFuture<String> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                Thread.sleep(2_000L);
                future.complete("Hendy Saputra");
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Test
    void create() throws ExecutionException, InterruptedException {
        Future<String> future = getValue();
        System.out.println(future.get());
    }

    private void execute(CompletableFuture<String> future, String value) {
        executor.execute(() -> {
            try {
                Thread.sleep(1_000L + random.nextInt(5000));
                future.complete(value);
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });
    }

    public Future<String> getFastest() {
        CompletableFuture<String> future = new CompletableFuture<>();

        execute(future, "Thread 1");
        execute(future, "Thread 2");
        execute(future, "Thread 3");

        return future;
    }

    @Test
    void testFastest() throws ExecutionException, InterruptedException {
        System.out.println(getFastest().get());
    }

    @Test
    void testCompletionStage() throws ExecutionException, InterruptedException {
        CompletableFuture<String[]> future = getValue().thenApply(string -> string.toUpperCase()).thenApply(string -> string.split(" "));

        var result = future.get();

        for (var string : result) {
            System.out.println(string);
        }
    }
}
