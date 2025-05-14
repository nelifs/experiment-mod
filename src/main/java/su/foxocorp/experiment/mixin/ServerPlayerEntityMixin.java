package su.foxocorp.experiment.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.foxocorp.experiment.common.ServerEventPayload;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void disableBedSpawn(ServerPlayerEntity.Respawn respawn, boolean sendMessage, CallbackInfo ci) {
        ci.cancel();
    }
}