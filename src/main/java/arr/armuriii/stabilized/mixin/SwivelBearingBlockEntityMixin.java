package arr.armuriii.stabilized.mixin;

import arr.armuriii.stabilized.util.StabilizedLockingSettings;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import dev.ryanhcode.sable.api.physics.constraint.rotary.RotaryConstraintHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.simulated_team.simulated.content.blocks.swivel_bearing.SwivelBearingBlock;
import dev.simulated_team.simulated.content.blocks.swivel_bearing.SwivelBearingBlockEntity;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static arr.armuriii.stabilized.util.RotationUtils.*;

@Debug(export = true)
@Mixin(value = SwivelBearingBlockEntity.class,remap = false)
public abstract class SwivelBearingBlockEntityMixin {

    @Shadow @Final private static MutableComponent SCROLL_OPTION_TITLE;
    @Shadow private double targetAngleDegrees;

    @Shadow protected abstract SubLevel getContainingSubLevel();

    @Shadow @Final @NotNull private SwivelBearingBlockEntity.@NotNull SwivelBearingCogwheelBlockEntity cogwheel;
    @Shadow private @Nullable RotaryConstraintHandle handle;

    @Shadow
    @Nullable
    protected abstract SubLevel getAttachedSubLevel();

    @Unique
    private ScrollOptionBehaviour<StabilizedLockingSettings> stabilized$lockedDefaultOption;

    @Unique
    private double stabilized$lastStabilizedAngle = 0;

    @ModifyArg(method = "addBehaviours",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/blockEntity/behaviour/scrollValue/ScrollOptionBehaviour;<init>(Ljava/lang/Class;Lnet/minecraft/network/chat/Component;Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;Lcom/simibubi/create/foundation/blockEntity/behaviour/ValueBoxTransform;)V"),index = 3)
    private ValueBoxTransform storeSelectionMode(ValueBoxTransform slot, @Share("SelectionModeValueBox")LocalRef<ValueBoxTransform> localRef) {
        localRef.set(slot);
        return slot;
    }


    @ModifyArg(method = "addBehaviours", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private Object swapBehavior(Object original, @Share("SelectionModeValueBox")LocalRef<ValueBoxTransform> localRef) {
        this.stabilized$lockedDefaultOption = new ScrollOptionBehaviour<>(StabilizedLockingSettings.class,
                SCROLL_OPTION_TITLE,
                (SwivelBearingBlockEntity)(Object)this,
                localRef.get());
        this.stabilized$lockedDefaultOption.value = 1;
        return this.stabilized$lockedDefaultOption;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ldev/simulated_team/simulated/content/blocks/swivel_bearing/SwivelBearingBlockEntity$LockingSetting;shouldLock(I)Z"))
    private boolean useStabilized(SwivelBearingBlockEntity.LockingSetting instance, int signal, Operation<Boolean> original) {
        return stabilized$lockedDefaultOption.get().shouldLock(signal);
    }

    @Definition(id = "attached", local = @Local(type = SubLevel.class, name = "attached"))
    @Expression("attached != null")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    // this doesn't change "attached != null", it only changes this.targetAngleDegrees AFTER it was calculated
    // couldn't find a better place than at the if statement right after ...
    private boolean changeBehavior(boolean original, @Local(name = "shouldLock") final boolean shouldLock) {
        if (this.stabilized$lockedDefaultOption.get().stabilizationCoef() == 0)
            return original; // if the mode is not stabilized, return prematurely

        if (!shouldLock)
            return original; // if the stabilization is deactivated, return prematurely

        var self = (SwivelBearingBlockEntity)(Object)this;

        this.targetAngleDegrees = 0; // if the bearing is not on a sublevel

        var attached = this.getAttachedSubLevel();
        var containing = this.getContainingSubLevel();
        if (containing != null) {
            var q = containing.logicalPose().orientation();
            var facing = self.getBlockState().getValue(SwivelBearingBlock.FACING);
            this.targetAngleDegrees = AngleHelper.angleLerp(
                    Math.sqrt(Math.abs(this.cogwheel.getSpeed()))/16, // sqrt(256)/16 = 1
                    this.stabilized$lastStabilizedAngle,
                    -Math.toDegrees(
                            getAxisAngle(new Quaternionf(q),facing)
                    )
            )*Math.signum(this.cogwheel.getSpeed())
            *this.stabilized$lockedDefaultOption.get().stabilizationCoef();
        }
        if (Double.isNaN(this.targetAngleDegrees))
            this.targetAngleDegrees = 0;

        this.targetAngleDegrees %= 360;

        if (attached instanceof ServerSubLevel serverAttached && containing instanceof ServerSubLevel serverContaining)
            if (Math.sqrt(serverContaining.getMassTracker().getMass())<serverAttached.getMassTracker().getMass())
                this.targetAngleDegrees = AngleHelper.angleLerp(
                        (
                                Math.sqrt(serverContaining.getMassTracker().getMass())
                                        /
                                        serverAttached.getMassTracker().getMass()
                        )/1.5,
                        this.stabilized$lastStabilizedAngle,
                        this.targetAngleDegrees
                );

        this.stabilized$lastStabilizedAngle = this.targetAngleDegrees;
        return original;
    }

    @WrapOperation(method = "updateServoCoefficients", at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/config/ConfigBase$ConfigFloat;get()Ljava/lang/Object;",ordinal = 2))
    private Object modifyDamping(ConfigBase.ConfigFloat instance, Operation<Object> original) {
        if (this.stabilized$lockedDefaultOption.get().stabilizationCoef() == 0)
            return original.call(instance);
        return instance.get()/4;
    }
}
