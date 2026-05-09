package arr.armuriii.stabilized.index;

import arr.armuriii.stabilized.Stabilized;
import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingBlock;
import arr.armuriii.stabilized.content.stabilizer_bearing.link_block.StabilizerBearingPlateBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class StabilizedBlocks {
    private static final CreateRegistrate REGISTRATE = Stabilized.getRegistrate();


        /*public static final BlockEntry<AbsorberBlock> ABSORBER =
            Simulated.getRegistrate().block("absorber", AbsorberBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate((ctx, prov) -> {
                        prov.horizontalBlock(ctx.get(), blockState -> prov.models()
                                .getExistingFile(prov.modLoc("block/" + ctx.getName() + "/block" + (blockState.getValue(BlockStateProperties.POWERED) ? "_powered" : ""))));
                    })
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                            .pattern("G")
                            .pattern("S")
                            .pattern("R")
                            .define('G', Blocks.COPPER_GRATE)
                            .define('S', Blocks.SPONGE)
                            .define('R', Items.REDSTONE)
                            .unlockedBy("has_ingredient", RegistrateRecipeProvider.has(SimTags.Items.REDSTONE_DUST))
                            .save(p))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item().transform(customItemModel())
                    .register();*/

    public static final BlockEntry<StabilizerBearingBlock> STABILIZER_BEARING =
            REGISTRATE.block("stabilizer_bearing", StabilizerBearingBlock::new)
                    .initialProperties(SharedProperties::netheriteMetal)
                    .properties((properties -> properties.destroyTime(5f)))
                    .blockstate((ctx, prov) -> prov.directionalBlock(ctx.getEntry(),
                            blockState -> prov.models().getExistingFile(
                                    prov.modLoc("block/stabilizer_bearing/block" + (blockState.getValue(StabilizerBearingBlock.ASSEMBLED) ? "_assembled" : "")))))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .item().transform(customItemModel("stabilizer_bearing", "item"))
                    .register();

    public static final BlockEntry<StabilizerBearingPlateBlock> STABILIZER_BEARING_LINK_BLOCK =
            REGISTRATE.block("stabilizer_bearing_link_block", StabilizerBearingPlateBlock::new)
                    .blockstate((ctx, prov) ->
                            prov.directionalBlock(ctx.getEntry(), blockState -> prov.models().getExistingFile(prov.modLoc("block/stabilizer_bearing/bearing_plate"))))
                    .initialProperties(SharedProperties::netheriteMetal)
                    .properties(properties -> properties
                            .destroyTime(5f))
                    .loot((p, b) -> p.dropOther(b, STABILIZER_BEARING.get()))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .register();


    public static void register() {
    }
}
