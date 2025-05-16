package su.foxocorp.experiment.client.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncUtils {

    public static CompletableFuture<Void> waitForAsync(long milliseconds) {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public static void runThenWaitAsync(Runnable runnable, long milliseconds) {
        CompletableFuture.runAsync(() -> {
            runnable.run();
            try {
                TimeUnit.MILLISECONDS.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}