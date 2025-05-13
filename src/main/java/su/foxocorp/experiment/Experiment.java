package su.foxocorp.experiment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import su.foxocorp.experiment.modules.WorldBorder;

public class Experiment implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("Experiment initialized");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            WorldBorder worldBorder = new WorldBorder();
            worldBorder.tick(server);
        });
    }
}
