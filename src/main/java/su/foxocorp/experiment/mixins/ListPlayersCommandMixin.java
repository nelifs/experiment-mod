package su.foxocorp.experiment.mixins;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ListCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Mixin(ListCommand.class)
public class ListPlayersCommandMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void modifyPlayerList(ServerCommandSource source, Function<ServerPlayerEntity, Text> nameProvider, CallbackInfoReturnable<Integer> cir) {
        PlayerManager playerManager = source.getServer().getPlayerManager();

        source.sendFeedback(() -> Text.of("На сервере никого нет..."), false);
        cir.setReturnValue(0);
    }
}
