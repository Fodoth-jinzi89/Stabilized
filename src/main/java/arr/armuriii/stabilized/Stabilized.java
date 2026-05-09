package arr.armuriii.stabilized;

import arr.armuriii.stabilized.index.StabilizedAdvancements;
import arr.armuriii.stabilized.index.StabilizedBlockEntityTypes;
import arr.armuriii.stabilized.index.StabilizedBlocks;
import com.mojang.logging.LogUtils;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlockEntity;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.index.SimBlocks;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import dev.simulated_team.simulated.util.SimColors;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(Stabilized.MOD_ID)
public class Stabilized {

    public static final String MOD_ID = "stabilized";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final NonNullSupplier<SimulatedRegistrate> REGISTRATE = NonNullSupplier.lazy(() ->
            (SimulatedRegistrate)(new SimulatedRegistrate(path(MOD_ID), MOD_ID)).defaultCreativeTab((ResourceKey)null));

    public static SimulatedRegistrate getRegistrate() {
        return REGISTRATE.get();
    }

    public Stabilized(IEventBus modEventBus, ModContainer modContainer) {
        setTooltips();

        StabilizedAdvancements.register();
        StabilizedBlocks.register();
        StabilizedBlockEntityTypes.register();

        getRegistrate().registerEventListeners(modEventBus);
        modEventBus.addListener(Stabilized::setup);
    }

    public static void setTooltips() {
        getRegistrate().setTooltipModifierFactory(item -> {
            final Rarity rarity = item.getDefaultInstance().getRarity();
            FontHelper.Palette color = FontHelper.Palette.STANDARD_CREATE;
            if (rarity == Rarity.EPIC)
                color = new FontHelper.Palette(TooltipHelper.styleFromColor(SimColors.EPIC_OURPLE), rarity.getStyleModifier().apply(Style.EMPTY));

            return new ItemDescription
                    .Modifier(item, color)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockStressValues.IMPACTS.register(StabilizedBlocks.STABILIZER_BEARING.get(),()->4);
        });
    }

    public static ResourceLocation path(final String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }
}
