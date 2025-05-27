package su.foxocorp.experiment.module;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.common.ServerEventPayload;
import su.foxocorp.experiment.event.FakePlayerEvent;
import su.foxocorp.experiment.event.PhantomBlocksInteractionsEvent;

import java.util.HashMap;
import java.util.List;

public class ServerEvents {

    // name : isClientSide
    private static final HashMap<String, Boolean> EVENT_TYPES = new HashMap<>();
    static {
        EVENT_TYPES.put("phantomBlocksInteractions", false);
        EVENT_TYPES.put("hideTabListEvent", true);
        EVENT_TYPES.put("changeRenderDistanceEvent", true);
        EVENT_TYPES.put("changeWindowTitle", true);
        EVENT_TYPES.put("sendMessageToActionBar", true);
        EVENT_TYPES.put("testEvent", true);
        EVENT_TYPES.put("fakePlayer", false);
    }

    public static boolean isClientSide(String eventType) {
        return EVENT_TYPES.getOrDefault(eventType, false);
    }

    private static final List<String> WINDOW_TITLES = List.of(
            "Они следят за тобой...",
            "Ты уверен, что это все не иллюзия?",
            "Тебе не стоило сюда заходить",
            "..."
    );

    private static final List<String> ACTION_BAR_MESSAGES = List.of(
            "Ты не одинок...",
            "Они ждут тебя...",
            "§aВы чувствуете чье-то злобное внимание...",
            "А вдруг, это все сон?"
    );

    private static final PhantomBlocksInteractionsEvent phantomBlocksInteractionsEvent = new PhantomBlocksInteractionsEvent();

    private static final FakePlayerEvent fakePlayerEvent = new FakePlayerEvent();

    public static void sendClientEventToAllPlayers(String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);

        for (ServerWorld world : Experiment.minecraftServer.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                ServerPlayNetworking.send(player, packet);
            }
        }
    }

    public static void sendClientEventToSpecificPlayer(ServerPlayerEntity player, String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);
        ServerPlayNetworking.send(player, packet);
    }

    public static void executeServerEventOnAllPlayers(String eventType, String args) {
        for (ServerWorld world : Experiment.minecraftServer.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                switch (eventType) {
                    case "phantomBlocksInteractions": phantomBlocksInteractionsEvent.handle(player); break;
                    case "fakePlayer": fakePlayerEvent.handle(player, args); break;
                }
            }
        }
    }

    public static void executeServerEventOnSpecificPlayer(ServerPlayerEntity player, String eventType, String args) {
        switch (eventType) {
            case "phantomBlocksInteractions": phantomBlocksInteractionsEvent.handle(player); break;
            case "fakePlayer": fakePlayerEvent.handle(player, args); break;
        }
    }

    public static void tick(MinecraftServer server) {
        if (server.getTicks() % 1000 == 0) {
            String event = EVENT_TYPES.keySet().toArray(new String[0])[Experiment.RANDOM.nextInt(EVENT_TYPES.size())];

            String args = switch (event) {
                case "changeWindowTitle" -> WINDOW_TITLES.get(Experiment.RANDOM.nextInt(WINDOW_TITLES.size()));
                case "changeRenderDistanceEvent" -> "16";
                case "sendMessageToActionBar" -> ACTION_BAR_MESSAGES.get(Experiment.RANDOM.nextInt(ACTION_BAR_MESSAGES.size()));
                default -> "";
            };

            if (isClientSide(event)) {
                if (Experiment.RANDOM.nextBoolean()) {
                    sendClientEventToAllPlayers(event, args);
                } else {
                    if (!server.getPlayerManager().getPlayerList().isEmpty()) {
                        ServerPlayerEntity randomPlayer = server.getPlayerManager()
                                .getPlayerList()
                                .get(Experiment.RANDOM.nextInt(server.getPlayerManager().getPlayerList().size()));

                        if (randomPlayer == null) return;
                        sendClientEventToSpecificPlayer(randomPlayer, event, args);
                    }
                }
            } else {
                if (Experiment.RANDOM.nextBoolean()) {
                    executeServerEventOnAllPlayers(event, args);
                } else {
                    if (!server.getPlayerManager().getPlayerList().isEmpty()) {
                        ServerPlayerEntity randomPlayer = server.getPlayerManager()
                                .getPlayerList()
                                .get(Experiment.RANDOM.nextInt(server.getPlayerManager().getPlayerList().size()));

                        if (randomPlayer == null) return;
                        executeServerEventOnSpecificPlayer(randomPlayer, event, args);
                    }
                }
            }
        }
    }
}
