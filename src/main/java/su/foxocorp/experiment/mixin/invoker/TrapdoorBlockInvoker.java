package su.foxocorp.experiment.mixin.invoker;

import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TrapdoorBlock.class)
public interface TrapdoorBlockInvoker {

    // АЛО ЛЮК!!? ДА ДА САЛЬТО
    @Invoker("flip")
    void doFlip(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player);
}
