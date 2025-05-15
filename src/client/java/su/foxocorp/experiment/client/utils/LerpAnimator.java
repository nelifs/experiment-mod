package su.foxocorp.experiment.client.utils;

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
}
