package su.foxocorp.experiment.client.event;

import net.minecraft.client.MinecraftClient;
import su.foxocorp.experiment.client.ExperimentClient;

public class TestEvent {

    public static void handleTestEvent() {
        MinecraftClient client = ExperimentClient.client;
    }
}