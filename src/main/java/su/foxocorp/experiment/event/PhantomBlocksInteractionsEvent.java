package su.foxocorp.experiment.event;

import net.minecraft.block.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.mixin.invoker.TrapdoorBlockInvoker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PhantomBlocksInteractionsEvent {

    public static void handle(ServerPlayerEntity player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        Map<TrapdoorBlock, List<BlockPos>> trapdoors = new HashMap<>();
        Map<DoorBlock, List<BlockPos>> doors = new HashMap<>();
        Map<ButtonBlock, List<BlockPos>> buttons = new HashMap<>();
        Map<LeverBlock, List<BlockPos>> levers = new HashMap<>();

        ServerWorld world = player.getServerWorld();

        synchronized (world) {
            for (int i = -32; i <= 32; i++) {
                for (int k = -32; k <= 32; k++) {
                    for (int j = -32; j <= 32; j++) {
                        BlockPos pos = new BlockPos((int) (x + i), (int) (y + k), (int) (z + j));
                        BlockState state = world.getBlockState(pos);

                        if (state.getBlock() instanceof DoorBlock) {
                            doors.computeIfAbsent((DoorBlock) state.getBlock(), k1 -> new ArrayList<>()).add(pos);
                        } else if (state.getBlock() instanceof TrapdoorBlock) {
                            trapdoors.computeIfAbsent((TrapdoorBlock) state.getBlock(), k1 -> new ArrayList<>()).add(pos);
                        } else if (state.getBlock() instanceof ButtonBlock) {
                            buttons.computeIfAbsent((ButtonBlock) state.getBlock(), k1 -> new ArrayList<>()).add(pos);
                        } else if (state.getBlock() instanceof LeverBlock) {
                            levers.computeIfAbsent((LeverBlock) state.getBlock(), k1 -> new ArrayList<>()).add(pos);
                        }
                    }
                }
            }
        }

        CompletableFuture.runAsync(() -> processTrapdoors(trapdoors, world));
        CompletableFuture.runAsync(() -> processDoors(doors, world));
        CompletableFuture.runAsync(() -> processButtons(buttons, world));
        CompletableFuture.runAsync(() -> processLevers(levers, world));
    }

    private static void processTrapdoors(Map<TrapdoorBlock, List<BlockPos>> trapdoors, ServerWorld world) {
        trapdoors.forEach((block, positions) -> positions.forEach(pos -> {
            int iterations = 10 + Experiment.RANDOM.nextInt(61);
            for (int i = 0; i < iterations; i++) {
                int randomDelay = 50 + Experiment.RANDOM.nextInt(251);
                CompletableFuture.delayedExecutor(i * randomDelay, TimeUnit.MILLISECONDS).execute(() -> {
                    world.getServer().execute(() -> {
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof TrapdoorBlock) {
                            ((TrapdoorBlockInvoker) block).doFlip(state, world, pos, null);
                        }
                    });
                });
            }
        }));
    }

    private static void processDoors(Map<DoorBlock, List<BlockPos>> doors, ServerWorld world) {
        doors.forEach((block, positions) -> positions.forEach(pos -> {
            int iterations = 10 + Experiment.RANDOM.nextInt(61);
            for (int i = 0; i < iterations; i++) {
                int randomDelay = 50 + Experiment.RANDOM.nextInt(251);
                CompletableFuture.delayedExecutor(i * randomDelay, TimeUnit.MILLISECONDS).execute(() -> {
                    world.getServer().execute(() -> {
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof DoorBlock) {
                            block.setOpen(null, world, state, pos, !state.get(DoorBlock.OPEN));
                        }
                    });
                });
            }
        }));
    }

    private static void processButtons(Map<ButtonBlock, List<BlockPos>> buttons, ServerWorld world) {
        buttons.forEach((block, positions) -> positions.forEach(pos -> {
            int iterations = 10 + Experiment.RANDOM.nextInt(61);
            for (int i = 0; i < iterations; i++) {
                int randomDelay = 50 + Experiment.RANDOM.nextInt(251);
                CompletableFuture.delayedExecutor(i * randomDelay, TimeUnit.MILLISECONDS).execute(() -> {
                    world.getServer().execute(() -> {
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof ButtonBlock) {
                            block.powerOn(state, world, pos, null);
                        }
                    });
                });
            }
        }));
    }

    private static void processLevers(Map<LeverBlock, List<BlockPos>> levers, ServerWorld world) {
        levers.forEach((block, positions) -> positions.forEach(pos -> {
            int iterations = 10 + Experiment.RANDOM.nextInt(61);
            for (int i = 0; i < iterations; i++) {
                int randomDelay = 50 + Experiment.RANDOM.nextInt(251);
                CompletableFuture.delayedExecutor(i * randomDelay, TimeUnit.MILLISECONDS).execute(() -> {
                    world.getServer().execute(() -> {
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof LeverBlock) {
                            block.togglePower(state, world, pos, null);
                        }
                    });
                });
            }
        }));
    }
}