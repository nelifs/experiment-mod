package su.foxocorp.experiment.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import su.foxocorp.experiment.Experiment;

import java.util.Random;

public class RandomUtils {
    private static Random random = Experiment.random;

    public static ServerPlayerEntity getRandomPlayer() {
        MinecraftServer server = Experiment.minecraftServer;

        if (!server.getPlayerManager().getPlayerList().isEmpty()) {

            return server.getPlayerManager()
                    .getPlayerList()
                    .get(random.nextInt(server.getPlayerManager().getPlayerList().size()));
        }
        return null;
    }
}
