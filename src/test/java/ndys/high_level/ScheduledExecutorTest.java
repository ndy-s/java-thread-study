package ndys.high_level;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorTest {

    @Test
    void delayedJob() throws InterruptedException {
        var executor = Executors.newScheduledThreadPool(10);

        var future = executor.schedule(() -> {
            System.out.println("Hello ScheduledExecutorTest");
        }, 5, TimeUnit.SECONDS);

        System.out.println(future.getDelay(TimeUnit.MILLISECONDS));
        
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void periodicJob() throws InterruptedException {
        var executor = Executors.newScheduledThreadPool(10);

        var future = executor.scheduleAtFixedRate(() -> {
            System.out.println("Hello ScheduledExecutorTest");
        }, 2, 2, TimeUnit.SECONDS);

        System.out.println(future.getDelay(TimeUnit.MILLISECONDS));

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
