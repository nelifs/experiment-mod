package su.foxocorp.experiment.client.utils;

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

    public static CompletableFuture<Void> runThenWaitAsync(Runnable runnable, long milliseconds) {
        return CompletableFuture.runAsync(() -> {
            runnable.run();
            try {
                TimeUnit.MILLISECONDS.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}