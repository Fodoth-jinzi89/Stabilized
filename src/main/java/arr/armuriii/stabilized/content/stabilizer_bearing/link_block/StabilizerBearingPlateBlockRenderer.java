package arr.armuriii.stabilized.content.stabilizer_bearing.link_block;

import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.simulated_team.simulated.index.SimPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class StabilizerBearingPlateBlockRenderer extends KineticBlockEntityRenderer<StabilizerBearingPlateBlockEntity> {

    public StabilizerBearingPlateBlockRenderer(final BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(final StabilizerBearingPlateBlockEntity be, final BlockState state) {
        return CachedBuffers.partialFacing(SimPartialModels.SHAFT_SIXTEENTH, state, state.getValue(StabilizerBearingBlock.FACING));
    }
}
