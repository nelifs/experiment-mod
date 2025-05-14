package su.foxocorp.experiment.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import su.foxocorp.experiment.client.network.ServerEventHandler;
import su.foxocorp.experiment.common.ServerEventPayload;

public class ExperimentClient implements ClientModInitializer {
    public static MinecraftClient client;

    private static final ServerEventHandler eventHandler = new ServerEventHandler();

    @Override
    public void onInitializeClient() {
        client = MinecraftClient.getInstance();

        ClientPlayNetworking.registerGlobalReceiver(ServerEventPayload.ID, (payload, context) -> {
            eventHandler.handleEvent(payload);
        });
    }
}