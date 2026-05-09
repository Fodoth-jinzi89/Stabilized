package arr.armuriii.stabilized.mixin;

import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingBlock;
import arr.armuriii.stabilized.index.StabilizedBlocks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.simulated_team.simulated.util.assembly.SimAssemblyContraption;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SimAssemblyContraption.class)
public class SimAssemblyContraptionMixin {

    @WrapOperation(method = "moveBlock", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean allowGyroscopicBearing(BlockEntry<?> instance, BlockState state, Operation<Boolean> original) {
        return original.call(instance, state) || StabilizedBlocks.STABILIZER_BEARING.has(state);
    }

    @ModifyArg(method = "moveSwivelBearing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
    private Property<?> useGyroscopicState(Property<?> par1, @Local(argsOnly = true)BlockState state) {
        if (StabilizedBlocks.STABILIZER_BEARING.has(state))
            return StabilizerBearingBlock.FACING; // not sure if it's mandatory ...
        return par1;
    }
}
