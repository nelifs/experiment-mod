package su.foxocorp.experiment.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import su.foxocorp.experiment.module.ServerEvents;
import su.foxocorp.experiment.util.RandomUtils;

import java.util.Objects;

public class ForceEventCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("forceEvent")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("event", StringArgumentType.string())
                        .then(CommandManager.argument("allUsers", BoolArgumentType.bool())
                                .executes(context -> execute(context, StringArgumentType.getString(context, "event"), BoolArgumentType.getBool(context, "allUsers"), ""))
                                .then(CommandManager.argument("args", StringArgumentType.greedyString())
                                        .executes(context -> execute(context, StringArgumentType.getString(context, "event"), BoolArgumentType.getBool(context, "allUsers"), StringArgumentType.getString(context, "args")))
                                )
                        )
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String event, boolean allUsers, String args) {

        if (allUsers) {
            ServerEvents.setEventToAllPlayers(event, args);
            Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("Event " + event + " sent to all players"), false);
        } else {
            ServerPlayerEntity player = RandomUtils.getRandomPlayer();
            if (player != null) {
                ServerEvents.setEventToPlayer(player, event, args);
                Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("Event " + event + " sent to random player (" + player.getName() + ")"), false);
            }
        }

        return 1;
    }
}
