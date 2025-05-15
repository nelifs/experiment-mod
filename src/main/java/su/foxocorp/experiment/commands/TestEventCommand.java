package su.foxocorp.experiment.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import su.foxocorp.experiment.module.ServerEvents;

import java.util.Objects;

public class TestEventCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("testevent")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("event", StringArgumentType.string())
                        .then(CommandManager.argument("args", StringArgumentType.greedyString())
                                .executes(context -> execute(context, StringArgumentType.getString(context, "event"), StringArgumentType.getString(context, "args"), false))
                                .then(CommandManager.argument("allUsers", BoolArgumentType.bool())
                                        .executes(context -> execute(context, StringArgumentType.getString(context, "event"), StringArgumentType.getString(context, "args"), BoolArgumentType.getBool(context, "allUsers")))
                                )
                        ))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String event, String args, boolean allUsers) {

        if (allUsers) {
            ServerEvents.setEventToAllPlayers(event, args);
            Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("Event " + event + " sent to all players"), false);
        } else {
            ServerEvents.setEventToPlayer(context.getSource().getPlayer(), event, args);
            Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("Event " + event + " sent to player"), false);
        }

        return 1;
    }
}
