package su.foxocorp.experiment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import su.foxocorp.experiment.common.ServerEventPayload;
import su.foxocorp.experiment.module.ActionBar;
import su.foxocorp.experiment.module.ServerEvents;
import su.foxocorp.experiment.module.WorldBorder;

public class Experiment implements ModInitializer {
    public static final String MOD_ID = "experiment";

    public static final Identifier ID = Identifier.of(MOD_ID, "server_event");

    private static final WorldBorder worldBorder = new WorldBorder();

    private static final ActionBar actionBar = new ActionBar();

    public static MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        System.out.println("Experiment Mod initialized");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> minecraftServer = server);

        PayloadTypeRegistry.playS2C().register(ServerEventPayload.ID, ServerEventPayload.CODEC);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            worldBorder.tick(server);
            actionBar.tick(server);
        });
    }
}