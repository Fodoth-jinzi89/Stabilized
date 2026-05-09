package arr.armuriii.stabilized.util;

import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import dev.ryanhcode.sable.api.block.BlockEntitySubLevelActor;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

import java.lang.Math;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class RotationUtils {

    public static double getAxisAngle(Quaternionf q, Direction direction) {
        final Vector3d ld = JOMLConversion.toJOML(Vec3.atLowerCornerOf(Direction.DOWN.getNormal()));
        q.transformInverse(ld);

        Vector3d forward = JOMLConversion.toJOML(Vec3.atLowerCornerOf(Direction.NORTH.getNormal()));
        q.transformInverse(forward);

        var XAngle = atan2(ld.z(), -ld.y());
        var YAngle = atan2(forward.x(), -forward.z());
        var ZAngle = -atan2(ld.x(), -ld.y());
        return switch (direction.getAxis()) {
            case X -> XAngle;
            case Y -> YAngle;
            case Z -> ZAngle;
        }*(direction.getAxisDirection()== Direction.AxisDirection.NEGATIVE ? -1 : 1);
    }

    public static Vector3d getAxisAngles(Quaternionf q) {
        var x = getAxisAngle(q,Direction.EAST);
        if (Math.abs(x)>Math.PI/2)
            x=(Math.abs(x)-Math.PI)*Math.signum(x);
        var y = getAxisAngle(q,Direction.UP);
        if (Math.abs(y)>Math.PI/2)
            y=(Math.abs(y)-Math.PI)*Math.signum(y);
        var z = getAxisAngle(q,Direction.SOUTH);
        if (Math.abs(z)>Math.PI/2)
            z=(Math.abs(z)-Math.PI)*Math.signum(z);

        return new Vector3d(x,y,z);
    }

    public static void stabilize(ServerSubLevel subLevel, RigidBodyHandle handle, Direction.Axis axis, double factor) {
        Vector3d vector3d = RotationUtils.getAxisAngles(new Quaternionf(subLevel.logicalPose().orientation()));
        vector3d.mul(-sqrt(subLevel.getMassTracker().getMass()));
        vector3d.mul(factor);
        switch (axis) {
            case X -> vector3d.x = 0;
            case Y -> vector3d.y = 0;
            case Z -> vector3d.z = 0;
        }

        handle.applyAngularImpulse(vector3d);
    }
}
