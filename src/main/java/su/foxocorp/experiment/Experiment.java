package su.foxocorp.experiment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.foxocorp.experiment.command.ForceEventCommand;
import su.foxocorp.experiment.command.TestEventCommand;
import su.foxocorp.experiment.common.ModHandshakePayload;
import su.foxocorp.experiment.common.ServerEventPayload;
import su.foxocorp.experiment.module.ActionBar;
import su.foxocorp.experiment.module.ServerEvents;
import su.foxocorp.experiment.module.WorldBorder;

import java.util.Arrays;
import java.util.Random;
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

    private static final ServerEvents serverEvents = new ServerEvents();

    public static MinecraftServer minecraftServer;

    private final ConcurrentMap<UUID, Boolean> handshakePending = new ConcurrentHashMap<>();

    private static final long HANDSHAKE_TIMEOUT_MS = 1000;

    private ScheduledExecutorService scheduler;

    private static final String REQUIRED_MOD_VERSION = "0.0.1";

    public static final Random RANDOM = new Random();

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Experiment mod initialized.");

        scheduler = Executors.newSingleThreadScheduledExecutor();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> minecraftServer = server);

        PayloadTypeRegistry.playS2C().register(ServerEventPayload.ID, ServerEventPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(ModHandshakePayload.ID, ModHandshakePayload.CODEC);
        LOGGER.info("Experiment mod payloads registered.");

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            TestEventCommand.register(commandDispatcher);
            ForceEventCommand.register(commandDispatcher);
        });
        LOGGER.info("Experiment mod commands registered.");

        ServerPlayConnectionEvents.INIT.register(((handler, server) -> {
            if (server.isSingleplayer()) return;

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
            if (server.isSingleplayer()) return;

            ServerPlayerEntity player = handler.player;
            UUID playerUuid = player.getUuid();
            handshakePending.remove(playerUuid);
        }));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (server.isSingleplayer()) return;

            scheduler.shutdownNow();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOGGER.warn("Experiment mod scheduler did not terminate in time.");
                }
            } catch (InterruptedException e) {
                LOGGER.error("Exception occurred during server shutdown:\n{}", Arrays.toString(e.getStackTrace()));
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isSingleplayer()) return;

            worldBorder.tick(server);
            actionBar.tick(server);
            serverEvents.tick(server);
        });
    }
}