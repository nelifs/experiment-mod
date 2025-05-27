package su.foxocorp.experiment.event;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import su.foxocorp.experiment.Experiment;

import java.util.UUID;

public class FakePlayerEvent {

    public static void handle(ServerPlayerEntity player, String name) {
        GameProfile fakeProfile = new GameProfile(UUID.randomUUID(), name);

        World world = player.getWorld();

        if (world instanceof ServerWorld serverWorld) {
            FakePlayer fakePlayer = FakePlayer.get(serverWorld, fakeProfile);

            fakePlayer.setPosition(player.getX(), player.getY(), player.getZ());
            fakePlayer.setHeadYaw(player.getHeadYaw());
            fakePlayer.setBodyYaw(player.getBodyYaw());

            serverWorld.getServer().getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, fakePlayer));

            serverWorld.spawnEntity(fakePlayer);
            fakePlayer.setVelocity(player.getVelocity());
        }
    }
}
