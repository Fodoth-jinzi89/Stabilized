package arr.armuriii.stabilized.util;

import arr.armuriii.stabilized.Stabilized;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.gui.AllIcons;
import dev.simulated_team.simulated.Simulated;

public enum StabilizedLockingSettings implements INamedIconOptions {
    LOCKED_ALWAYS(AllIcons.I_CONFIG_LOCKED, Simulated.MOD_ID+".generic."+"swivel_default_always_locked"),
    LOCKED_DEFAULT(AllIcons.I_CONFIG_LOCKED, Simulated.MOD_ID+".generic."+"swivel_default_locked"),
    UNLOCKED_DEFAULT(AllIcons.I_CONFIG_UNLOCKED, Simulated.MOD_ID+".generic."+"swivel_default_unlocked"),
    UNLOCKED_ALWAYS(AllIcons.I_CONFIG_UNLOCKED, Simulated.MOD_ID+".generic."+"swivel_default_always_unlocked"),
    STABILIZED_CLOCKWISE(AllIcons.I_REFRESH, Stabilized.MOD_ID +".generic."+"swivel_cw_stabilized"),
    STABILIZED_COUNTER_CLOCKWISE(AllIcons.I_ROTATE_CCW, Stabilized.MOD_ID +".generic."+"swivel_ccw_stabilized");

    private final String translationKey;
    private final AllIcons icon;
    StabilizedLockingSettings(final AllIcons icon, final String name) {
        this.icon = icon;
        this.translationKey = name;
    }
    @Override
    public AllIcons getIcon() {
        return this.icon;
    }
    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }

    public boolean shouldLock(final int signal) {
        if (this == STABILIZED_CLOCKWISE) return true;
        if (this == STABILIZED_COUNTER_CLOCKWISE) return true;
        if (this == UNLOCKED_ALWAYS) return false;
        if (this == LOCKED_ALWAYS) return true;
        return signal > 0 != (this == LOCKED_DEFAULT);
    }
    public int stabilizationCoef() {
        if (this == STABILIZED_CLOCKWISE) return 1;
        if (this == STABILIZED_COUNTER_CLOCKWISE) return -1;
        return 0;
    }
}
