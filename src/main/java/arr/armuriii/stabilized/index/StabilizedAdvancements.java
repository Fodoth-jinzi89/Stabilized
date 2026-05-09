package arr.armuriii.stabilized.index;

import arr.armuriii.stabilized.Stabilized;
import arr.armuriii.stabilized.advancement.critereon.GyroscopicBearingAssemblesTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StabilizedAdvancements {

    public static final DeferredRegister<net.minecraft.advancements.CriterionTrigger<?>> REGISTRATE =
            DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES,Stabilized.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>,GyroscopicBearingAssemblesTrigger> GYROSCOPIC_BEARING_ASSEMBLES_TRIGGER = REGISTRATE
            .register("gyroscopic_bearing_assembles",GyroscopicBearingAssemblesTrigger::new);

    public static void register() {

    }
}
