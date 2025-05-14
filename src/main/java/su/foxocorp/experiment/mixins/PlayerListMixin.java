package su.foxocorp.experiment.mixins;

import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerListS2CPacket.class)
public class PlayerListMixin {
    @Mutable
    @Shadow @Final private List<PlayerListS2CPacket.Entry> entries;

    //    @Inject(method = "getPlayerList", at = @At("RETURN"), cancellable = true)
//    private void getPlayerList(CallbackInfoReturnable<List<ServerPlayerEntity>> cir) {
//        cir.setReturnValue(new ArrayList<>());
//    }
//    @Inject(method = "getPlayerNames", at = @At("RETURN"), cancellable = true)
//    private void getPlayerNames(CallbackInfoReturnable<String[]> cir) {
//        cir.setReturnValue(new String[0]);
//    }
//    @Inject(method = "<init>*", at = @At("RETURN"))
//    private void onPacketInit(CallbackInfo ci) {
//        this.entries = new ArrayList<>();
//    }
}