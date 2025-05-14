package su.foxocorp.experiment.module;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.common.ServerEventPayload;

public class ServerEvents {
    public static void setEventToAllPlayers(String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);

        for (ServerWorld world : Experiment.minecraftServer.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                ServerPlayNetworking.send(player, packet);
            }
        }
    }

    public static void setEventToPlayer(ServerPlayerEntity player, String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);
        ServerPlayNetworking.send(player, packet);
    }
}
