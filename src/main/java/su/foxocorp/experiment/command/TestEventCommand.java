package su.foxocorp.experiment.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import su.foxocorp.experiment.module.ServerEvents;

import java.util.Objects;

public class TestEventCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("testEvent")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("event", StringArgumentType.string())
                        .then(CommandManager.argument("args", StringArgumentType.greedyString())
                                .executes(context -> execute(context, StringArgumentType.getString(context, "event"), StringArgumentType.getString(context, "args")))
                        ))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String event, String args) {

        ServerEvents.setEventToPlayer(context.getSource().getPlayer(), event, args);
        Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.of("Event " + event + " sent to player"), false);

        return 1;
    }
}
