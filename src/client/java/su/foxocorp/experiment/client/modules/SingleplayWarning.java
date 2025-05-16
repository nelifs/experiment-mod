package su.foxocorp.experiment.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SingleplayWarning {
    public static void showWarning(MinecraftClient client) {
        if (client.isInSingleplayer() || client.isConnectedToLocalServer() || client.isIntegratedServerRunning()) {
            if (client.player != null) {
                client.player.sendMessage(
                        Text.translatable("experiment.warning.singleplayer"),
                        false
                );
            }
        }
    }
}
