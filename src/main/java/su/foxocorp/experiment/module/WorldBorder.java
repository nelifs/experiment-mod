package su.foxocorp.experiment.module;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class WorldBorder {

    private static final double BORDER_SIZE = 500.0;

    private int TICK_COUNTER = 0;

    private static final int SOUND_PER_TICKS = 15;

    public void tick(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            if (world.getRegistryKey() == ServerWorld.END) continue;

            for (PlayerEntity player : world.getPlayers()) {
                TICK_COUNTER++;

                double x = player.getX();
                double z = player.getZ();
                float distanceFromBorder = (float) (Math.sqrt(x * x + z * z) - 1000) / 4;

                if (Math.abs(x) > BORDER_SIZE || Math.abs(z) > BORDER_SIZE) {
                    if (TICK_COUNTER >= SOUND_PER_TICKS) {
                        TICK_COUNTER = 0;
                        player.playSoundToPlayer(SoundEvents.BLOCK_TRIAL_SPAWNER_AMBIENT_OMINOUS, SoundCategory.AMBIENT, distanceFromBorder / 7, .2f);
                    }

                    StatusEffectInstance EFFECT_INSTANCE = new StatusEffectInstance(StatusEffects.DARKNESS, 3 * 20, 1, true, false, false);

                    player.addStatusEffect(EFFECT_INSTANCE);
                    player.damage(world, world.getDamageSources().outOfWorld(), distanceFromBorder);
                }
            }
        }
    }
}
