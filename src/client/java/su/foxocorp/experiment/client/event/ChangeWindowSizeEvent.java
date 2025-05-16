package su.foxocorp.experiment.client.event;

import net.minecraft.client.MinecraftClient;
import su.foxocorp.experiment.client.ExperimentClient;

import java.util.Random;

public class ChangeWindowSizeEvent {

    public static void changeWindowSize() {
        MinecraftClient client = ExperimentClient.client;

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            client.getWindow().setWindowedSize(random.nextInt(1920), random.nextInt(1080));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
