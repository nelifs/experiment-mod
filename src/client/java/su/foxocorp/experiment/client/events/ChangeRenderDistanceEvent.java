package su.foxocorp.experiment.client.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.utils.AsyncUtils;

public class ChangeRenderDistanceEvent {
    public static void changeRenderDistance(int distance) {
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        client.player.playSoundToPlayer(SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1.0f, 1f);

        int oldViewDistance = client.options.getViewDistance().getValue();
        AsyncUtils.waitForAsync(300).thenRun(() -> {
            client.options.getViewDistance().setValue(distance);
        });

        AsyncUtils.waitForAsync(1000 * 60).thenRun(() -> {
            if (client.options.getViewDistance().getValue() != oldViewDistance) {
                client.options.getViewDistance().setValue(oldViewDistance);
            }
        });
    }
}
