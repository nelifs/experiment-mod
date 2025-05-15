package su.foxocorp.experiment.client.utils;

import java.util.function.Consumer;

public class LerpAnimator {
    private float currentValue;
    private float targetValue;
    private final float smoothingFactor;

    public LerpAnimator(float initialValue, float factor) {
        this.currentValue = initialValue;
        this.smoothingFactor = factor;
        this.targetValue = initialValue;
    }

    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void update() {
        float tolerance = 0.001F;
        if (Math.abs(currentValue - targetValue) <= tolerance) {
            currentValue = targetValue;
            return;
        }

        currentValue = currentValue + smoothingFactor * (targetValue - currentValue);
    }

    public void process(int iterations, int delay, Consumer<Float> updater) {
        for (int i = 0; i < iterations; i++) {
            update();
            updater.accept(currentValue);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}