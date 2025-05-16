package su.foxocorp.experiment.client.module;

import net.minecraft.client.MinecraftClient;

public class LowViewDistance {

    public void tick(MinecraftClient client) {
        if (!client.options.getViewDistance().getValue().equals(4)) {
            client.options.getViewDistance().setValue(4);
        }
    }
}
