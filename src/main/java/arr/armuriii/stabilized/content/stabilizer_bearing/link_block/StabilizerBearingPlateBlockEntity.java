package arr.armuriii.stabilized.content.stabilizer_bearing.link_block;

import arr.armuriii.stabilized.content.stabilizer_bearing.StabilizerBearingBlockEntity;
import arr.armuriii.stabilized.index.StabilizedBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StabilizerBearingPlateBlockEntity extends KineticBlockEntity implements BlockEntitySubLevelActor {

    private BlockPos parent;
    private UUID parentSubLevelId;
    private boolean assembling;

    public StabilizerBearingPlateBlockEntity(final BlockEntityType<?> typeIn, final BlockPos pos, final BlockState state) {
        super(typeIn, pos, state);
    }

    /**
     * Called before we disasemble/assemble the swivel bearing plate
     */
    public void beforeAssembly() {
        this.assembling = true;
    }

    @Override
    public void remove() {
        // if the block was broken / destroyed, destroy our parent
        if (!Objects.requireNonNull(this.level).isClientSide && !this.assembling) {
            this.destroyBearing();
        }

        super.remove();
    }

    private void destroyBearing() {
        if (this.parent != null && Objects.requireNonNull(this.getLevel()).getBlockState(this.parent).is(StabilizedBlocks.STABILIZER_BEARING))
            this.getLevel().destroyBlock(this.parent, false);
    }

    public void setParent(final StabilizerBearingBlockEntity be) {
        final SubLevel subLevel = Sable.HELPER.getContaining(be);

        this.parent = be.getBlockPos();
        this.parentSubLevelId = subLevel != null ? subLevel.getUniqueId() : null;
    }

    @Override
    public float propagateRotationTo(final KineticBlockEntity target, final BlockState stateFrom, final BlockState stateTo, final BlockPos diff, final boolean connectedViaAxes, final boolean connectedViaCogs) {
        return this.parent != null && target.equals(Objects.requireNonNull(this.level).getBlockEntity(this.parent)) ? 1 : super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
    }

    @Override
    public boolean isCustomConnection(final KineticBlockEntity other, final BlockState state, final BlockState otherState) {
        return this.parent != null && other.equals(Objects.requireNonNull(this.level).getBlockEntity(this.parent));
    }

    @Override
    public List<BlockPos> addPropagationLocations(final IRotate block, final BlockState state, final List<BlockPos> neighbours) {
        if (this.parent != null) {
            neighbours.add(this.parent);
        }

        return super.addPropagationLocations(block, state, neighbours);
    }

    @Override
    protected void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        if (this.parent != null) {
            compound.put("ParentPos", NbtUtils.writeBlockPos(this.parent));
        }

        if (this.parentSubLevelId != null) {
            compound.putUUID("ParentSubLevelId", this.parentSubLevelId);
        }
    }

    @Override
    protected void read(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        if (NbtUtils.readBlockPos(compound, "parent").isPresent())
            this.parent = NbtUtils.readBlockPos(compound, "parent").get();


        if (NbtUtils.readBlockPos(compound, "ParentPos").isPresent())
            this.parent = NbtUtils.readBlockPos(compound, "ParentPos").get();

        if (compound.contains("ParentSubLevelId"))
            this.parentSubLevelId = compound.getUUID("ParentSubLevelId");
    }

    @Override
    public void sable$physicsTick(final ServerSubLevel subLevel, final RigidBodyHandle handle, final double timeStep) {
        if (this.parent != null) {
            final BlockEntity parentBE = Objects.requireNonNull(this.level).getBlockEntity(this.parent);

            if (parentBE instanceof final StabilizerBearingBlockEntity swivelBearingBlockEntity) {
                swivelBearingBlockEntity.updateServoCoefficients();
            }
        }
    }

    @Override
    public @Nullable Iterable<@NotNull SubLevel> sable$getConnectionDependencies() {
        if (this.parent == null) {
            return null;
        }

        final SubLevelContainer container = SubLevelContainer.getContainer(this.level);

        if (this.parentSubLevelId != null) {
            final SubLevel subLevel = Objects.requireNonNull(container).getSubLevel(this.parentSubLevelId);

            if (subLevel != null) {
                return List.of(subLevel);
            }
        }

        return null;
    }


    public void setParentAssembleNextTick() {
        final BlockEntity be = Objects.requireNonNull(this.level).getBlockEntity(this.parent);
        if (be instanceof final StabilizerBearingBlockEntity sbe) {
            sbe.assembleNextTick = true;
        }
    }

    public void fixParentLinkingWhenMoved() {
        if (Objects.requireNonNull(this.level).isClientSide() || this.parent == null) {
            return;
        }

        final BlockEntity be = this.level.getBlockEntity(this.parent);

        if (be instanceof final StabilizerBearingBlockEntity sbe) {
            sbe.setPlatePos(this.getBlockPos());

            final SubLevel newSublevel = Sable.HELPER.getContaining(this);
            if (newSublevel != null) {
                final UUID subLevelID = sbe.getSubLevelID();
                final UUID newID = newSublevel.getUniqueId();

                if (newID != subLevelID) {
                    sbe.setSubLevelID(newSublevel.getUniqueId());
                    sbe.reattachConstraint(newSublevel, true);
                }
            }
        }
    }
}