package su.foxocorp.experiment.modules;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class WorldBorder {

    private static final double BORDER_SIZE = 1000.0;

    public void tick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            world.getWorldBorder().setCenter(0.0, 0.0);
            world.getWorldBorder().setSize(BORDER_SIZE * 2);

            for (ServerPlayerEntity player : world.getPlayers()) {
                double x = player.getX();
                double z = player.getZ();
                if (Math.abs(x) > BORDER_SIZE || Math.abs(z) > BORDER_SIZE) {
                    player.kill(world);
                }
            }
        }
    }
}
