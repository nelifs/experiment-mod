package su.foxocorp.experiment.client.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.util.AsyncUtils;
import su.foxocorp.experiment.client.util.LerpAnimator;

public class ChangeRenderDistanceEvent {

    public static float originalFogEndDistance = 64F;

    public static float currentFogEndDistance = 64F;

    private static void setCurrentFogEndDistance(float distance) {
        currentFogEndDistance = distance;
    }

    public static void handle(float distance) {
        MinecraftClient client = MinecraftClient.getInstance();

        originalFogEndDistance = client.options.getViewDistance().getValue() * 16;

        // im pretty sure that in a normal scenario, it won't cause any overlapping if it is without lerp animation but just in case
        if (originalFogEndDistance != currentFogEndDistance) {
            AsyncUtils.runThenWaitAsync(() -> {
                LerpAnimator animator = new LerpAnimator(currentFogEndDistance, 0.5f);
                animator.setTargetValue(originalFogEndDistance);

                animator.process(60, 100, ChangeRenderDistanceEvent::setCurrentFogEndDistance);
            }, 1000);
        }

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
