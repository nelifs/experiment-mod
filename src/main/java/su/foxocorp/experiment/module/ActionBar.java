package su.foxocorp.experiment.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ActionBar {

    public void tick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            for (PlayerEntity player : world.getPlayers()) {
                Item mainHand = player.getMainHandStack().getItem();
                Item offHand = player.getOffHandStack().getItem();

                int x = Math.toIntExact(Math.round(player.getX()));
                int y = Math.toIntExact(Math.round(player.getY()));
                int z = Math.toIntExact(Math.round(player.getZ()));

                long day = world.getTimeOfDay() / 24000;

                if (mainHand.equals(Items.COMPASS) || offHand.equals(Items.COMPASS) ) {
                    player.sendMessage(Text.of(String.format("§6X§f: %d §6Y§f: %d §6Z§f: %d", x, y, z)), true);
                } else if (mainHand.equals(Items.CLOCK) || offHand.equals(Items.CLOCK)) {
                    player.sendMessage(Text.of(String.format("§6День§f: %d", day)), true);
                }
            }
        }
    }
}
