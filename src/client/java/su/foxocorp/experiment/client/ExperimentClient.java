package su.foxocorp.experiment.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.client.modules.LowBrightness;
import su.foxocorp.experiment.client.modules.LowViewDistance;
import su.foxocorp.experiment.client.modules.SingleplayWarning;
import su.foxocorp.experiment.client.network.ServerEventHandler;
import su.foxocorp.experiment.common.ModHandshakePayload;
import su.foxocorp.experiment.common.ServerEventPayload;

import java.util.Random;

public class ExperimentClient implements ClientModInitializer {
    public static MinecraftClient client;

    private static final ServerEventHandler eventHandler = new ServerEventHandler();

    private static final LowBrightness lowBrightness = new LowBrightness();

    private static LowViewDistance lowViewDistance = new LowViewDistance();

    public static final Random random = new Random();

    @Override
    public void onInitializeClient() {
        client = MinecraftClient.getInstance();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.isInSingleplayer() || client.isConnectedToLocalServer()) {
                SingleplayWarning.showWarning(client);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerEventPayload.ID, (payload, context) -> {
            eventHandler.handleEvent(payload);
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ModHandshakePayload payload = new ModHandshakePayload(getModVersion());
            ClientPlayNetworking.send(payload);
            System.out.println("Sent mod version " + getModVersion() + " to server.");
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