package su.foxocorp.experiment.client.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import su.foxocorp.experiment.client.ExperimentClient;

import java.util.concurrent.CompletableFuture;

public class SendMessageToActionBarEvent {

    public static void handleEvent(String message) {
        MinecraftClient client = ExperimentClient.client;

        if (client != null && client.player != null) {
            StringBuilder scrambledText = new StringBuilder();

            for (int i = 0; i < message.length(); i++) {
                if (message.charAt(i) != ' ') {
                    scrambledText.append(message.charAt(ExperimentClient.RANDOM.nextInt(message.length() - 1)));
                } else {
                    scrambledText.append(' ');
                }
            }

            CompletableFuture.runAsync(() -> {
                try {
                    for (int i = 0; i < scrambledText.length(); i++) {
                        String currentText = scrambledText.substring(0, i + 1);
                        client.execute(() -> client.player.sendMessage(Text.of(currentText), true));
                        Thread.sleep(50);
                    }

                    for (int i = 0; i < message.length(); i++) {
                        scrambledText.setCharAt(i, message.charAt(i));
                        String currentText = scrambledText.toString();
                        client.execute(() -> client.player.sendMessage(Text.of(currentText), true));
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
}