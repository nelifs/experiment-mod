package su.foxocorp.experiment.client.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.ExperimentClient;
import su.foxocorp.experiment.client.utils.AsyncUtils;

import java.util.concurrent.CompletableFuture;

public class ChangeWindowTitleEvent {
    public static void changeWindowTitle(String title) {
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
                }, client).thenCompose(ignored -> AsyncUtils.waitForAsync(50));
            }
            chain = chain.thenRunAsync(() -> text.append(" "), client)
                    .thenCompose(ignored -> AsyncUtils.waitForAsync(200));
        }

        chain.thenRunAsync(() -> client.getWindow().setTitle(" "), client)
                .thenCompose(ignored -> AsyncUtils.waitForAsync(5000));
    }
}