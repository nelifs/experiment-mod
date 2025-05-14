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

@Mixin(value = BackgroundRenderer.class, priority = 500)
@Environment(EnvType.CLIENT)
public class BackgroundRendererMixin {

    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void onApplyFog(net.minecraft.client.render.Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickProgress, CallbackInfoReturnable<Fog> cir) {
        Fog originalParameters = cir.getReturnValue();

        Fog modifiedParameters = new Fog(
                0.0F,
                originalParameters.end(),
                originalParameters.shape(),
                originalParameters.red(),
                originalParameters.green(),
                originalParameters.blue(),
                originalParameters.alpha()
        );

        cir.setReturnValue(modifiedParameters);
    }
}
