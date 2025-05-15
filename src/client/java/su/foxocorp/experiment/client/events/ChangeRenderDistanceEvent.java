package su.foxocorp.experiment.client.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import su.foxocorp.experiment.client.utils.AsyncUtils;
import su.foxocorp.experiment.client.utils.LerpAnimator;

public class ChangeRenderDistanceEvent {

    public static float originalFogEndDistance = 64F;

    public static float currentFogEndDistance = 64F;

    public static void changeRenderDistance(float distance) {
        MinecraftClient client = MinecraftClient.getInstance();

        originalFogEndDistance = client.options.getViewDistance().getValue() * 16;

        assert client.player != null;
        client.player.playSoundToPlayer(SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1.0f, 1f);

        AsyncUtils.waitForAsync(300).thenRun(() -> {
            LerpAnimator animator = new LerpAnimator(currentFogEndDistance, 0.5f);
            animator.setTargetValue(distance);

            for (int i = 0; i < 60; i++) {
                animator.update();
                currentFogEndDistance = animator.getCurrentValue();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        AsyncUtils.waitForAsync(1000 * 60).thenRun(() -> {
            LerpAnimator animator = new LerpAnimator(currentFogEndDistance, 0.5f);
            animator.setTargetValue(originalFogEndDistance);

            for (int i = 0; i < 60; i++) {
                animator.update();
                currentFogEndDistance = animator.getCurrentValue();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
