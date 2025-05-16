package su.foxocorp.experiment.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.client.modules.LowBrightness;
import su.foxocorp.experiment.client.modules.LowViewDistance;
import su.foxocorp.experiment.client.modules.SingleplayerWarning;
import su.foxocorp.experiment.client.network.ServerEventHandler;
import su.foxocorp.experiment.common.ModHandshakePayload;
import su.foxocorp.experiment.common.ServerEventPayload;

import java.util.Random;

public class ExperimentClient implements ClientModInitializer {

    public static MinecraftClient client;

    private static final ServerEventHandler eventHandler = new ServerEventHandler();

    private static final LowBrightness lowBrightness = new LowBrightness();

    private static final LowViewDistance lowViewDistance = new LowViewDistance();

    public static final Random RANDOM = new Random();

    public static final Logger LOGGER = LoggerFactory.getLogger(Experiment.MOD_ID);

    @Override
    public void onInitializeClient() {
        client = MinecraftClient.getInstance();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.isInSingleplayer() || client.isConnectedToLocalServer()) {
                SingleplayerWarning.showWarning(client);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerEventPayload.ID, (payload, context) -> {
            eventHandler.handleEvent(payload);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ModHandshakePayload payload = new ModHandshakePayload(getModVersion());
            ClientPlayNetworking.send(payload);

            LOGGER.info("Sent mod version {} to server.", getModVersion());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isConnectedToLocalServer()) {
                lowBrightness.tick(client);
                lowViewDistance.tick(client);
            }
        });
    }

    private String getModVersion() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(Experiment.MOD_ID).orElse(null);
        if (modContainer != null) {
            return modContainer.getMetadata().getVersion().getFriendlyString();
        }
        return null;
    }
}