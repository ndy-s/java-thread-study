package ndys.low_level;

public class Balance {

    private long value;

    public Balance(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public static void transfer(Balance from, Balance to, Long value) throws InterruptedException {
        // Problem deadlock
//        synchronized (from) {
//            Thread.sleep(1_000L);
//
//            synchronized (to) {
//                Thread.sleep(1_000L);
//                from.setValue(from.getValue() - value);
//                to.setValue(to.getValue() + value);
//            }
//        }

        // Deadlock solution
        synchronized (from) {
            Thread.sleep(1_000L);
            from.setValue(from.getValue() - value);
        }

        synchronized (to) {
            Thread.sleep(1_000L);
            to.setValue(to.getValue() + value);
        }
    }
}
