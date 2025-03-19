package practice;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Practice1Test {

    /**
     * Creating and running a basic thread using the Thread class.
     * <p>
     * Task:
     * Create a thread that prints numbers from 1 to 5 with a 1-second delay between each print.
     * Start the thread and let it run.
     * <p>
     * Example:
     * Output (approximate, due to timing):
     * Thread-0: 1
     * (1 sec delay)
     * Thread-0: 2
     * (1 sec delay)
     * Thread-0: 3
     * (1 sec delay)
     * Thread-0: 4
     * (1 sec delay)
     * Thread-0: 5
     */
    @Test
    void basicThread() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }

    /**
     * Implementing a Runnable to count even numbers in a list using multiple threads.
     * <p>
     * Task:
     * Given a list of integers, split the work between two threads. Each thread should count the number of even numbers in its portion of the list.
     * Combine the results from both threads to get the total count.
     * <p>
     * Example:
     * Input:  [1, 2, 3, 4, 5, 6, 7, 8]
     * Thread 1 processes: [1, 2, 3, 4] -> 2 evens
     * Thread 2 processes: [5, 6, 7, 8] -> 2 evens
     * Output: Total even numbers = 4
     */
    @Test
    void evenNumbers() throws InterruptedException {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        int mid = numbers.size() / 2;

        class EvenCounter implements Runnable {
            private final List<Integer> subList;
            private int count;

            EvenCounter(List<Integer> subList) {
                this.subList = subList;
            }

            @Override
            public void run() {
                count = (int) subList.stream().filter(i -> i % 2 == 0).count();
            }

            public int getCount() {
                return count;
            }
        }

        EvenCounter counter1 = new EvenCounter(numbers.subList(0, mid));
        EvenCounter counter2 = new EvenCounter(numbers.subList(mid, numbers.size()));

        Thread thread1 = new Thread(counter1);
        Thread thread2 = new Thread(counter2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        int total = counter1.getCount() + counter2.getCount();
        System.out.println("Total even numbers: " + total);
    }

    /**
     * Synchronizing threads to print a sequence alternately.
     * <p>
     * Task:
     * Create two threads. One prints even numbers (0, 2, 4, 6, 8), and the other prints odd numbers (1, 3, 5, 7, 9).
     * Use synchronization to ensure they alternate, producing the sequence: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9.
     * <p>
     * Example:
     * Output:
     * EvenThread: 0
     * OddThread: 1
     * EvenThread: 2
     * OddThread: 3
     * EvenThread: 4
     * OddThread: 5
     * EvenThread: 6
     * OddThread: 7
     * EvenThread: 8
     * OddThread: 9
     */
    @Test
    void synchronizedAlternatePrinting() throws InterruptedException {
        class Printer {
            private boolean isEvenTurn = true;

            synchronized void printEven(int num) {
                while (!isEvenTurn) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("EvenThread: " + num);
                isEvenTurn = false;
                notify();
            }

            synchronized void printOdd(int num) {
                while (isEvenTurn) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("OddThread: " + num);
                isEvenTurn = true;
                notify();
            }
        }

        Printer printer = new Printer();

        Thread evenThread = new Thread(() -> {
            for (int i = 0; i <= 8; i += 2) {
                printer.printEven(i);
            }
        });

        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= 9; i += 2) {
                printer.printOdd(i);
            }
        });

        evenThread.start();
        oddThread.start();

        evenThread.join();
        oddThread.join();
    }

}
