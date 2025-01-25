package ballistix.common.tile.turret.antimissile.util;

import ballistix.api.turret.ITarget;
import ballistix.common.tile.radar.TileFireControlRadar;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class TileTurretAntimissileProjectile extends TileTurretAntimissile {

    public TileTurretAntimissileProjectile(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState, double range, double minRange, double usage, double rotationSpeedRadians) {
        super(tileEntityTypeIn, worldPos, blockState, range, minRange, usage, rotationSpeedRadians);
    }

    @Nullable
    @Override
    public Vec3 getTargetPosition(ITarget target) {

        float trackingSpeed = 0F;//radar.tracking.speed;
        Vec3 trackingVector = target.getTargetMovement();

        double timeToIntercept = TileFireControlRadar.getTimeToIntercept(target.getTargetLocation(), trackingVector, trackingSpeed, getProjectileSpeed(), getProjectileLaunchPosition());

        if(timeToIntercept <= 0) {
            return null;
        }

        return target.getTargetLocation().add(trackingVector.scale(trackingSpeed).scale(timeToIntercept));
    }

    // speed in units of ticks
    public abstract float getProjectileSpeed();

}
