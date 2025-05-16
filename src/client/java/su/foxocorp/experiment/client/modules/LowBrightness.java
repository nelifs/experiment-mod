package su.foxocorp.experiment.client.modules;

import net.minecraft.client.MinecraftClient;

public class LowBrightness {

    public void tick(MinecraftClient client) {
        if (!client.options.getGamma().getValue().equals(0.0)) {
            client.options.getGamma().setValue(0.0);
        }
    }
}
