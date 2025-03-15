package ndys.high_level.counter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock {

    private long value = 0L;

    // Use final to prevent reassignment and ensure thread safety.
    final private Lock lock = new ReentrantLock();

    public void increment() {
        try {
            lock.lock();
            value++;
        } finally {
            lock.unlock();
        }
    }

    public Long getValue() {
        return value;
    }
}
