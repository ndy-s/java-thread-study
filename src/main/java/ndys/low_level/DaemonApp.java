package ndys.low_level;

public class DaemonApp {

    public static void main(String[] args) {
        var thread = new Thread(() -> {
            try {
                Thread.sleep(3_000L);
                System.out.println("Run Thread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Daemon thread, don't wait thread
        // thread.setDaemon(true);

        // User thread, wait all thread to finish
        thread.start();
    }
}
