package arr.armuriii.stabilized;

import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingRenderer;
import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingVisual;
import arr.armuriii.stabilized.content.stabilizer_bearing.link_block.StabilizerBearingPlateBlockRenderer;
import arr.armuriii.stabilized.index.StabilizedBlockEntityTypes;
import arr.armuriii.stabilized.index.StabilizedPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.simulated_team.simulated.index.SimPartialModels;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Stabilized.MOD_ID,dist = Dist.CLIENT)
public class StabilizedClient {

    public StabilizedClient(IEventBus modBus) {
        StabilizedPartialModels.init();
        modBus.addListener(StabilizedClient::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(StabilizedBlockEntityTypes.STABILIZER_BEARING.get(), StabilizerBearingRenderer::new);
            BlockEntityRenderers.register(StabilizedBlockEntityTypes.STABILIZER_BEARING_LINK_BLOCK.get(), StabilizerBearingPlateBlockRenderer::new);

            SimpleBlockEntityVisualizer.builder(StabilizedBlockEntityTypes.STABILIZER_BEARING.get())
                    .factory(StabilizerBearingVisual::new)
                    .neverSkipVanillaRender()
                    .apply();

            SimpleBlockEntityVisualizer.builder(StabilizedBlockEntityTypes.STABILIZER_BEARING_LINK_BLOCK.get())
                    .factory(OrientedRotatingVisual.of(SimPartialModels.SHAFT_SIXTEENTH))
                    .neverSkipVanillaRender()
                    .apply();
        });
    }
}
