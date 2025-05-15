package su.foxocorp.experiment.client.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.utils.AsyncUtils;
import su.foxocorp.experiment.client.utils.LerpAnimator;

public class ChangeRenderDistanceEvent {

    public static float originalFogEndDistance = 64F;

    public static float currentFogEndDistance = 64F;

    public static void setCurrentFogEndDistance(float distance) {
        currentFogEndDistance = distance;
    }

    public static void changeRenderDistance(float distance) {
        MinecraftClient client = MinecraftClient.getInstance();

        originalFogEndDistance = client.options.getViewDistance().getValue() * 16;

        assert client.player != null;
        client.player.playSoundToPlayer(SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1.0f, 1f);

        AsyncUtils.waitForAsync(300).thenRun(() -> {
            LerpAnimator animator = new LerpAnimator(currentFogEndDistance, 0.5f);
            animator.setTargetValue(distance);

            animator.process(60, 100, ChangeRenderDistanceEvent::setCurrentFogEndDistance);
        });

        AsyncUtils.waitForAsync(1000 * 60).thenRun(() -> {
            LerpAnimator animator = new LerpAnimator(currentFogEndDistance, 0.5f);
            animator.setTargetValue(originalFogEndDistance);

            animator.process(60, 100, ChangeRenderDistanceEvent::setCurrentFogEndDistance);
        });
    }
}
