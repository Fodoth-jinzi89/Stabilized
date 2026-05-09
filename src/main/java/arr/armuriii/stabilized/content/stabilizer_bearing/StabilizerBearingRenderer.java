package arr.armuriii.stabilized.content.stabilizer_bearing;

import arr.armuriii.stabilized.index.StabilizedPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.simulated_team.simulated.index.SimPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class StabilizerBearingRenderer extends KineticBlockEntityRenderer<StabilizerBearingBlockEntity> {

    public StabilizerBearingRenderer(final BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(final StabilizerBearingBlockEntity be, final float partialTicks, final PoseStack ms, final MultiBufferSource buffer, final int light, final int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) {
            return;
        }

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        final BlockState state = be.getBlockState();
        final Direction.Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);

        final SuperByteBuffer cogwheel = kineticRotationTransform(
                CachedBuffers.partialFacingVertical(StabilizedPartialModels.STABILIZER_BEARING_COG, state, state.getValue(StabilizerBearingBlock.FACING).getOpposite()),
                be.getExtraKinetics(),
                axis,
                getAngleForBe(be.getExtraKinetics(), be.getBlockPos(), axis),
                light);

        final VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        cogwheel.renderInto(ms, vb);
        if (!be.isAssembled())
            renderRotatingBuffer(be, CachedBuffers.partialFacing(SimPartialModels.SHAFT_SIXTEENTH, state, state.getValue(StabilizerBearingBlock.FACING)), ms, vb, light);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(final StabilizerBearingBlockEntity be, final BlockState state) {
        return CachedBuffers.partialFacing(SimPartialModels.SHAFT_SIXTEENTH, state, state.getValue(StabilizerBearingBlock.FACING).getOpposite());
    }
}
