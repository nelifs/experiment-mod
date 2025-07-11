package su.foxocorp.experiment.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Fog;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.foxocorp.experiment.client.ExperimentClient;
import su.foxocorp.experiment.client.event.ChangeRenderDistanceEvent;

@Mixin(value = BackgroundRenderer.class, priority = 500)
@Environment(EnvType.CLIENT)
public class BackgroundRendererMixin {

    @Inject(method = "toggleFog", at = @At("RETURN"), cancellable = true)
    private static void onToggleFog(CallbackInfoReturnable<Boolean> cir) {
        if (!ExperimentClient.client.isConnectedToLocalServer()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void onApplyFog(net.minecraft.client.render.Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickProgress, CallbackInfoReturnable<Fog> cir) {
        Fog originalParameters = cir.getReturnValue();

        Fog modifiedParameters = new Fog(
                0.0F,
                ChangeRenderDistanceEvent.currentFogEndDistance,
                originalParameters.shape(),
                originalParameters.red(),
                originalParameters.green(),
                originalParameters.blue(),
                originalParameters.alpha()
        );

        cir.setReturnValue(modifiedParameters);
    }
}
