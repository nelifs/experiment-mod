package su.foxocorp.experiment.event;

import net.minecraft.block.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import su.foxocorp.experiment.mixin.invoker.TrapdoorBlockInvoker;

public class PhantomBlocksInteractionsEvent {

    public static void handle(ServerPlayerEntity player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        for (int l = 0; l <= 5; l++) {
            player.sendMessage(Text.of(String.valueOf(l)));
            for (int i = -32; i <= 32; i++) {
                for (int k = -32; k <= 32; k++) {
                    for (int j = -32; j <= 32; j++) {
                        BlockPos pos = new BlockPos((int) (x + i), (int) (y + k), (int) (z + j));
                        ServerWorld world = player.getServerWorld();
                        BlockState state = world.getBlockState(pos);

                        if (state.getBlock() instanceof DoorBlock) {
                            boolean doorIsOpen = state.get(DoorBlock.OPEN);
                            ((DoorBlock) state.getBlock()).setOpen(null, world, state, pos, doorIsOpen);
                        } else if (state.getBlock() instanceof TrapdoorBlock) {
                            TrapdoorBlockInvoker block = (TrapdoorBlockInvoker) state.getBlock();
                            block.doFlip(state, world, pos, null);
                        } else if (state.getBlock() instanceof ButtonBlock) {
                            ((ButtonBlock) state.getBlock()).powerOn(state, world, pos, null);
                        } else if (state.getBlock() instanceof LeverBlock) {
                            ((LeverBlock) state.getBlock()).togglePower(state, world, pos, null);
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

