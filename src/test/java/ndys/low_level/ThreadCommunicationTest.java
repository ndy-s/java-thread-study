package ndys.low_level;

import org.junit.jupiter.api.Test;

public class ThreadCommunicationTest {

    private String message = null;

    @Test
    void manual() throws InterruptedException {
        var thread1 = new Thread(() -> {
            // Not recommended (will drain cpu resources)
            // Use wait and notify
            while (message == null) {
                // Wait for second thread
            }

            System.out.println(message);
        });

        var thread2 = new Thread(() -> {
            message = "NdyS Thread";
        });

        thread2.start();
        thread1.start();

        thread2.join();
        thread1.join();
    }

    @Test
    void waitNotify() throws InterruptedException {
        final var lock = new Object();

        var thread1 = new Thread(() -> {
            // Synchronize will off when call wait
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var thread2 = new Thread(() -> {
            synchronized (lock) {
                message = "NdyS Thread";
                lock.notify();
            }
        });

        // Wait run first before notify
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void waitNotifyAll() throws InterruptedException {
        final var lock = new Object();

        var thread1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("Thread 1: " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var thread3 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    System.out.println("Thread 3: " + message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var thread2 = new Thread(() -> {
            synchronized (lock) {
                message = "NdyS Thread";
                lock.notifyAll();
            }
        });

        thread1.start();
        thread3.start();
        thread2.start();

        thread1.join();
        thread3.join();
        thread2.join();
    }
}
