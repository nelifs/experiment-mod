package su.foxocorp.experiment.module;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.common.ServerEventPayload;

import java.util.List;

public class ServerEvents {
    public static final List<String> EVENT_TYPES = List.of("hideTabListEvent", "changeRenderDistanceEvent", "changeWindowTitle", "sendMessageToActionBar");

    public static final List<String> WINDOW_TITLES = List.of(
            "Они следят за тобой...",
            "Ты уверен, что это все не иллюзия?",
            "Тебе не стоило сюда заходить",
            "..."
    );

    public static final List<String> ACTION_BAR_MESSAGES = List.of(
            "Ты не одинок...",
            "Они ждут тебя...",
            "§aВы чувствуете чье-то злобное внимание...",
            "А вдруг, это все сон?"
    );

    public static void setEventToAllPlayers(String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);

        for (ServerWorld world : Experiment.minecraftServer.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                ServerPlayNetworking.send(player, packet);
            }
        }
    }

    public static void setEventToPlayer(ServerPlayerEntity player, String eventType, String args) {
        ServerEventPayload packet = new ServerEventPayload(eventType, args);
        ServerPlayNetworking.send(player, packet);
    }

    public static void tick(MinecraftServer server) {
        if (server.getTicks() % 1000 == 0) {
            String event = EVENT_TYPES.get(Experiment.random.nextInt(EVENT_TYPES.size()));

            String args = switch (event) {
                case "changeWindowTitle" -> WINDOW_TITLES.get(Experiment.random.nextInt(WINDOW_TITLES.size()));
                case "changeRenderDistanceEvent" -> "16";
                case "sendMessageToActionBar" -> ACTION_BAR_MESSAGES.get(Experiment.random.nextInt(ACTION_BAR_MESSAGES.size()));
                default -> "";
            };

            if (Experiment.random.nextBoolean()) {
                setEventToAllPlayers(event, args);
            } else {
                if (!server.getPlayerManager().getPlayerList().isEmpty()) {
                    ServerPlayerEntity randomPlayer = server.getPlayerManager()
                            .getPlayerList()
                            .get(Experiment.random.nextInt(server.getPlayerManager().getPlayerList().size()));

                    if (randomPlayer == null) return;
                    setEventToPlayer(randomPlayer, event, args);
                }
            }
        }
    }
}
