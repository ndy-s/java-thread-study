package ndys.low_level.counter;

public class SynchronizedCounter {

    private Long value = 0L;

    // Synchronized method (whole method)
//    public synchronized void increment() {
//        value++;
//    }

    // Synchronized statement (specific code)
    public void increment() {
        // this: current object for intrinsic lock
        synchronized (this) {
            value++;
        }
    }

    public Long getValue() {
        return value;
    }
}
