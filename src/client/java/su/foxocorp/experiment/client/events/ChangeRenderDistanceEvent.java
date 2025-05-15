package su.foxocorp.experiment.client.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.utils.AsyncUtils;

public class ChangeRenderDistanceEvent {

    public static float originalFogEndDistance = 64F;

    public static float currentFogEndDistance = 64F;

    public static void changeRenderDistance(float distance) {
        MinecraftClient client = MinecraftClient.getInstance();

        originalFogEndDistance = client.options.getViewDistance().getValue() * 16;

        assert client.player != null;
        client.player.playSoundToPlayer(SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1.0f, 1f);

        AsyncUtils.waitForAsync(300).thenRun(() -> {
            for (int i = 0; i < ((originalFogEndDistance - distance) / 4); i++) {

                currentFogEndDistance = currentFogEndDistance - 4F;

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        AsyncUtils.waitForAsync(1000 * 60).thenRun(() -> {
            for (int i = 0; i < ((originalFogEndDistance - distance) / 4); i++) {

                currentFogEndDistance = currentFogEndDistance + 4F;

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
}
