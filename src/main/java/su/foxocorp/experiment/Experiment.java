package su.foxocorp.experiment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import su.foxocorp.experiment.common.ModHandshakePayload;
import su.foxocorp.experiment.common.ServerEventPayload;
import su.foxocorp.experiment.module.ActionBar;
import su.foxocorp.experiment.module.ServerEvents;
import su.foxocorp.experiment.module.WorldBorder;

import java.util.UUID; // Импортируем UUID
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Experiment implements ModInitializer {
    public static final String MOD_ID = "experiment";

    public static final Identifier ID = Identifier.of(MOD_ID, "server_event");

    private static final WorldBorder worldBorder = new WorldBorder();

    private static final ActionBar actionBar = new ActionBar();

    public static MinecraftServer minecraftServer;

    private final ConcurrentMap<UUID, Boolean> handshakePending = new ConcurrentHashMap<>();

    private static final long HANDSHAKE_TIMEOUT_MS = 1000;

    private ScheduledExecutorService scheduler;

    private static final String REQUIRED_MOD_VERSION = "0.0.1";

    @Override
    public void onInitialize() {
        System.out.println("Experiment Mod initialized");

        scheduler = Executors.newSingleThreadScheduledExecutor();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> minecraftServer = server);

        PayloadTypeRegistry.playS2C().register(ServerEventPayload.ID, ServerEventPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModHandshakePayload.ID, ModHandshakePayload.CODEC);

        ServerPlayConnectionEvents.INIT.register(((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            UUID playerUuid = player.getUuid();

            handshakePending.put(playerUuid, true);

            scheduler.schedule(() -> {
                if (handshakePending.containsKey(playerUuid)) {
                    Text disconnectMessage = Text.literal("Handshake failed: Did not receive mod version packet within time limit.\nPlease ensure the mod is installed correctly.");

                    server.execute(() -> {
                        ServerPlayerEntity currentPlayer = server.getPlayerManager().getPlayer(playerUuid);
                        if (currentPlayer != null && currentPlayer.networkHandler != null) {
                            currentPlayer.networkHandler.disconnect(disconnectMessage);
                        }
                    });

                    handshakePending.remove(playerUuid);
                }
            }, HANDSHAKE_TIMEOUT_MS, TimeUnit.MILLISECONDS);

            ServerPlayNetworking.registerReceiver(handler, ModHandshakePayload.ID, (payload, context) -> {
                ServerPlayerEntity receivingPlayer = context.player();
                UUID receivingPlayerUuid = receivingPlayer.getUuid();

                handshakePending.remove(receivingPlayerUuid);

                String clientVersion = payload.version();

                if (!clientVersion.equals(REQUIRED_MOD_VERSION)) {
                    Text disconnectMessage = Text.literal("Mod version mismatch!\nRequired server version: " + REQUIRED_MOD_VERSION + "\nYour client version: " + clientVersion + "\nPlease update the mod.");
                    context.server().execute(() -> {
                        receivingPlayer.networkHandler.disconnect(disconnectMessage);
                    });
                }
            });
        }));

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            UUID playerUuid = player.getUuid();
            handshakePending.remove(playerUuid);
        }));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            scheduler.shutdownNow();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Handshake scheduler did not terminate in 5 seconds.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            worldBorder.tick(server);
            actionBar.tick(server);
        });
    }
}