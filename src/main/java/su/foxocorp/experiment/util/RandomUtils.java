package su.foxocorp.experiment.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import su.foxocorp.experiment.Experiment;

import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = Experiment.RANDOM;

    public static ServerPlayerEntity getRandomPlayer() {
        MinecraftServer server = Experiment.minecraftServer;

        if (!server.getPlayerManager().getPlayerList().isEmpty()) {

            return server.getPlayerManager()
                    .getPlayerList()
                    .get(RANDOM.nextInt(server.getPlayerManager().getPlayerList().size()));
        }
        return null;
    }
}
