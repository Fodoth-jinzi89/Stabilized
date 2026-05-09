package arr.armuriii.stabilized.advancement.critereon;

import arr.armuriii.stabilized.index.StabilizedAdvancements;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GyroscopicBearingAssemblesTrigger extends SimpleCriterionTrigger<GyroscopicBearingAssemblesTrigger.TriggerInstance> {

    public GyroscopicBearingAssemblesTrigger() {

    }

    public void trigger(ServerPlayer player, double distance) {
        this.trigger(player,triggerInstance -> triggerInstance.distance.matches(distance));
    }

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Doubles distance) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create((p_337349_) -> p_337349_
                .group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                MinMaxBounds.Doubles.CODEC.optionalFieldOf("distance", MinMaxBounds.Doubles.atLeast(10)).forGetter(TriggerInstance::distance))
                .apply(p_337349_, TriggerInstance::new));

        public static Criterion<TriggerInstance> assemblesGyroscopicBearing() {
            return StabilizedAdvancements.GYROSCOPIC_BEARING_ASSEMBLES_TRIGGER.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), MinMaxBounds.Doubles.atLeast(10)));
        }
    }
}
