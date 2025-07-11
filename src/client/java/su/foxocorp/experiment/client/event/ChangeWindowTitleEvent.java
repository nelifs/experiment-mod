package su.foxocorp.experiment.client.event;

import net.minecraft.client.MinecraftClient;
import su.foxocorp.experiment.client.ExperimentClient;
import su.foxocorp.experiment.client.util.AsyncUtils;

import java.util.concurrent.CompletableFuture;

public class ChangeWindowTitleEvent {

    public static void handle(String title) {
        MinecraftClient client = ExperimentClient.client;

        String[] words = title.split(" ");
        StringBuilder text = new StringBuilder();

        CompletableFuture<Void> chain = CompletableFuture.completedFuture(null);

        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                chain = chain.thenRunAsync(() -> {
                    text.append(letter);
                    client.getWindow().setTitle(text.toString());
                }, client).thenCompose(ignored -> AsyncUtils.waitForAsync(100));
            }
            chain = chain.thenRunAsync(() -> text.append(" "), client)
                    .thenCompose(ignored -> AsyncUtils.waitForAsync(300));
        }
        chain = chain.thenRunAsync(() -> {}).thenCompose(ignored -> AsyncUtils.waitForAsync(1000 * 60 * 5));

        chain.thenRunAsync(() -> client.getWindow().setTitle(" "), client);
    }
}