package su.foxocorp.experiment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import su.foxocorp.experiment.module.ActionBar;
import su.foxocorp.experiment.module.WorldBorder;

public class Experiment implements ModInitializer {

    public static final String MOD_ID = "experiment";

    private static final WorldBorder worldBorder = new WorldBorder();

    private static final ActionBar actionBar = new ActionBar();

    @Override
    public void onInitialize() {
        System.out.println("Experiment Mod initialized");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            worldBorder.tick(server);
            actionBar.tick(server);
        });
    }
}
