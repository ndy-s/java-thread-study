package ndys.high_level.fork_join;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForkJoinTest {

    @Test
    void create() {
        var forkJoinPool1 = ForkJoinPool.commonPool();
        var forkJoinPool2 = new ForkJoinPool(5);

        // Other ways
        var executor1 = Executors.newWorkStealingPool();
        var executor2 = Executors.newWorkStealingPool(5);
    }

    @Test
    void recursiveAction() throws InterruptedException {
        var pool = ForkJoinPool.commonPool();
        List<Integer> integers = IntStream.range(0, 1000).boxed().collect(Collectors.toList());

        var task = new SimpleFormJoinTask(integers);
        pool.submit(task);

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
    }

    public static class SimpleFormJoinTask extends RecursiveAction {
        private List<Integer> integers;

        public SimpleFormJoinTask(List<Integer> integers) {
            this.integers = integers;
        }

        @Override
        protected void compute() {
            if (integers.size() <= 10) {
                doExecute();
            } else {
                forkCompute();
            }
        }

        private void doExecute() {
            integers.forEach(integer -> System.out.println(Thread.currentThread().getName() + ": " + integer));
        }

        private void forkCompute() {
            List<Integer> integers1 = this.integers.subList(0, this.integers.size() / 2);
            List<Integer> integers2 = this.integers.subList(this.integers.size() / 2, this.integers.size());

            SimpleFormJoinTask task1 = new SimpleFormJoinTask(integers1);
            SimpleFormJoinTask task2 = new SimpleFormJoinTask(integers2);

            ForkJoinTask.invokeAll(task1, task2);
        }
    }

    @Test
    void recursiveTask() throws ExecutionException, InterruptedException {
        var pool = ForkJoinPool.commonPool();
        List<Integer> integers = IntStream.range(0, 1000).boxed().collect(Collectors.toList());

        TotalRecursiveTask task = new TotalRecursiveTask(integers);
        ForkJoinTask<Long> submit = pool.submit(task);

        Long aLong = submit.get();
        System.out.println("Fork Join Calculation: " + aLong);

        long sum = integers.stream().mapToLong(value -> value).sum();
        System.out.println("Single Thread Calculation: " + sum);

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);
    }

    public static class TotalRecursiveTask extends RecursiveTask<Long> {
        private List<Integer> integers;

        public TotalRecursiveTask(List<Integer> integers) {
            this.integers = integers;
        }

        @Override
        protected Long compute() {
            if (integers.size() <= 10) {
                return doCompute();
            } else {
                return forkCompute();
            }
        }

        private Long forkCompute() {
            List<Integer> integers1 = this.integers.subList(0, this.integers.size() / 2);
            List<Integer> integers2 = this.integers.subList(this.integers.size() / 2, this.integers.size());

            TotalRecursiveTask task1 = new TotalRecursiveTask(integers1);
            TotalRecursiveTask task2 = new TotalRecursiveTask(integers2);

            ForkJoinTask.invokeAll(task1, task2);

            return task1.join() + task2.join();
        }

        private Long doCompute() {
            return integers.stream().mapToLong(value -> value).peek(value -> System.out.println(Thread.currentThread().getName() + ": " + value)).sum();
        }
    }
}
