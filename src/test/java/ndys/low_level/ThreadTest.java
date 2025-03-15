package ndys.low_level;

import org.junit.jupiter.api.Test;

public class ThreadTest {

    @Test
    void mainThread() {
        var name = Thread.currentThread().getName();
        System.out.println(name);
    }

    @Test
    void createThread() {
        Runnable runnable = () -> {
            System.out.println("Hello from thread: " + Thread.currentThread().getName());
        };

        // Run in main thread (asynchronous)
        // runnable.run();

        // Run in custom thread (asynchronous)
        var thread = new Thread(runnable);
        thread.start();

        System.out.println("Program End.");
    }

    @Test
    void threadSleep() throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(2_000L);
                System.out.println("Hello from thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        var thread = new Thread(runnable);
        thread.start();

        System.out.println("Program End.");

        // For thread testing purpose, wait for 3s
        // Better solution, using thread join
        Thread.sleep(3_000L);
    }

    @Test
    void threadJoin() throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(2_000L);
                System.out.println("Hello from thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        System.out.println("Waiting..");

        // Waiting for the thread to finish executing
        thread.join();

        System.out.println("Program End.");
    }

    @Test
    void threadInterrupt() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                // Manual check if thread interrupted
                if (Thread.interrupted()) {
                    return;
                }

                System.out.println("Runnable : " + i);
                try {
                    Thread.sleep(1_000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        Thread.sleep(5_000L);
        thread.interrupt();

        System.out.println("Waiting...");
        thread.join();
        System.out.println("Program End.");
    }

    @Test
    void threadName() {
        var thread = new Thread(() -> {
            System.out.println("Run in thread: " + Thread.currentThread().getName());
        });

        thread.setName("NdyS");
        thread.start();
    }

    @Test
    void threadState() throws InterruptedException {
        var thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());
            System.out.println("Run in thread: " + Thread.currentThread().getName());
        });

        thread.setName("NdyS");
        System.out.println(thread.getState());

        thread.start();
        thread.join();
        System.out.println(thread.getState());
    }

    @Test
    void threadYield() throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " - iteration: " + i);
                Thread.yield();

                try {
                    Thread.sleep(1_000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        var thread1 = new Thread(task);
        var thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }




}
