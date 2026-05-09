package arr.armuriii.stabilized.index;

import arr.armuriii.stabilized.Stabilized;
import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingBlockEntity;
import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingRenderer;
import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingVisual;
import arr.armuriii.stabilized.content.stabilizer_bearing.link_block.StabilizerBearingPlateBlockEntity;
import arr.armuriii.stabilized.content.stabilizer_bearing.link_block.StabilizerBearingPlateBlockRenderer;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.simulated_team.simulated.index.SimPartialModels;

public class StabilizedBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = Stabilized.getRegistrate();

    /*public static final BlockEntityEntry<AbsorberBlockEntity> ABSORBER = Simulated.getRegistrate()
            .blockEntity("absorber", AbsorberBlockEntity::new)
            .validBlocks(StabilizedBlocks.ABSORBER)
            .renderer(() -> AbsorberRenderer::new)
            .register();*/

    public static final BlockEntityEntry<StabilizerBearingBlockEntity> STABILIZER_BEARING = REGISTRATE
            .blockEntity("stabilizer_bearing", StabilizerBearingBlockEntity::new)
            .visual(() -> StabilizerBearingVisual::new)
            .validBlocks(StabilizedBlocks.STABILIZER_BEARING)
            .renderer(() -> StabilizerBearingRenderer::new)
            .register();

    public static final BlockEntityEntry<StabilizerBearingPlateBlockEntity> STABILIZER_BEARING_LINK_BLOCK = REGISTRATE
            .blockEntity("stabilizer_bearing_link_block", StabilizerBearingPlateBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(SimPartialModels.SHAFT_SIXTEENTH))
            .validBlocks(StabilizedBlocks.STABILIZER_BEARING_LINK_BLOCK)
            .renderer(() -> StabilizerBearingPlateBlockRenderer::new)
            .register();


    public static void register() {
    }
}
