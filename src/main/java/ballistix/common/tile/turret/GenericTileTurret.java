package ballistix.common.tile.turret;

import ballistix.api.turret.ITarget;
import ballistix.common.tile.radar.TileFireControlRadar;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class GenericTileTurret extends GenericTile {

    public final double range;
    public final double rotationSpeedRadians;
    public final double usage;
    public final double minimumRange;

    public final Property<Vec3> turretRotation = property(new Property<>(PropertyTypes.VEC3, "turrot", getDefaultOrientation()));
    public final Property<Vec3> desiredRotation = property(new Property<>(PropertyTypes.VEC3, "currot", getDefaultOrientation()));
    public final Property<Vec3> targetMovement = property(new Property<>(PropertyTypes.VEC3, "movevec", Vec3.ZERO));
    public final Property<Boolean> hasTarget = property(new Property<>(PropertyTypes.BOOLEAN, "hastarget", false));
    public final Property<Boolean> hasNoPower = property(new Property<>(PropertyTypes.BOOLEAN, "haspower", false));
    public final Property<Boolean> inRange = property(new Property<>(PropertyTypes.BOOLEAN, "isrange", false));

    public GenericTileTurret(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState, double range, double minimumRange, double usage, double rotationSpeedRadians) {
        super(tileEntityTypeIn, worldPos, blockState);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(usage * 20));
        this.usage = usage;
        this.range = range;
        this.minimumRange = minimumRange;
        this.rotationSpeedRadians = rotationSpeedRadians;
    }


    public void tickServer(ComponentTickable tickable) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        hasNoPower.set(electro.getJoulesStored() < usage);

        if (hasNoPower.get() || !isValidPlacement()) {
            return;
        }

        tickServerActive(tickable);

        ITarget target = getTarget(tickable.getTicks());

        hasTarget.set(target != null);

        boolean canFire = false;

        double distanceToTarget = 0;

        if (hasTarget.get()) {

            Vec3 interceptionPos = getTargetPosition(target);

            if(interceptionPos != null) {

                Vec3 launchPos = getProjectileLaunchPosition();

                double deltaX = interceptionPos.x - launchPos.x;
                double deltaY = interceptionPos.y - launchPos.y;
                double deltaZ = interceptionPos.z - launchPos.z;

                double sumXZ = deltaX * deltaX + deltaZ * deltaZ;

                double magXZ = Math.sqrt(sumXZ);

                if(magXZ <= 0) {
                    magXZ = 1;
                }

                double thetaY = Math.atan(deltaY / magXZ);

                targetMovement.set(new Vec3(deltaX, deltaY, deltaZ).normalize());

                desiredRotation.set(new Vec3(deltaX / magXZ, Math.sin(thetaY), deltaZ / magXZ));

                distanceToTarget = TileFireControlRadar.getDistanceToMissile(launchPos, interceptionPos);

            }

        } else {
            desiredRotation.set(getDefaultOrientation());
        }

        inRange.set(distanceToTarget >= minimumRange && distanceToTarget <= range);

        if (turretRotation.get().equals(desiredRotation.get())) {

            canFire = hasTarget.get() && inRange.get();

        } else {

            double thetaDesiredXZ = getXZAngleRadians(desiredRotation.get());
            double thetaCurrXZ = getXZAngleRadians(turretRotation.get());

            double angleDifXZ = thetaDesiredXZ - thetaCurrXZ;

            double deltaY = desiredRotation.get().y - turretRotation.get().y;

            if (deltaY < 0) {
                turretRotation.set(turretRotation.get().add(0, -Math.cos(rotationSpeedRadians) * 0.125, 0));
                if (turretRotation.get().y < getMinElevation()) {
                    turretRotation.set(new Vec3(turretRotation.get().x, getMinElevation(), turretRotation.get().z));
                } else if (turretRotation.get().y < desiredRotation.get().y) {
                    turretRotation.set(new Vec3(turretRotation.get().x, desiredRotation.get().y, turretRotation.get().z));
                }
            } else if (deltaY > 0) {
                turretRotation.set(turretRotation.get().add(0, Math.cos(rotationSpeedRadians) * 0.125, 0));

                if (turretRotation.get().y > getMaxElevation()) {
                    turretRotation.set(new Vec3(turretRotation.get().x, getMaxElevation(), turretRotation.get().z));
                } else if (turretRotation.get().y > desiredRotation.get().y) {
                    turretRotation.set(new Vec3(turretRotation.get().x, desiredRotation.get().y, turretRotation.get().z));
                }
            }

            if (angleDifXZ >= 0) {

                thetaCurrXZ += rotationSpeedRadians;

            } else {

                thetaCurrXZ -= rotationSpeedRadians;

            }

            //thetaCurrXZ = getXZAngleRadians(turretRotation.get());

            if (angleDifXZ >= 0 && thetaCurrXZ > thetaDesiredXZ) {

                turretRotation.set(new Vec3(desiredRotation.get().x, turretRotation.get().y, desiredRotation.get().z));

            } else if (angleDifXZ < 0 && thetaCurrXZ < thetaDesiredXZ) {

                turretRotation.set(new Vec3(desiredRotation.get().x, turretRotation.get().y, desiredRotation.get().z));

            } else {
                turretRotation.set(new Vec3(Math.cos(thetaCurrXZ), turretRotation.get().y, Math.sin(thetaCurrXZ)));
            }

            canFire = hasTarget.get() && turretRotation.get().equals(desiredRotation.get()) && inRange.get();

        }

        if (canFire) {
            fireTickServer();
        }

    }

    public abstract void tickServerActive(ComponentTickable tickable);
    public abstract void fireTickServer();

    public abstract Vec3 getDefaultOrientation();

    public abstract Vec3 getProjectileLaunchPosition();

    @Nullable
    public abstract Vec3 getTargetPosition(@Nonnull ITarget target);

    public abstract double getMinElevation();

    public abstract double getMaxElevation();

    @Nullable
    public abstract ITarget getTarget(long ticks);

    public abstract boolean isValidPlacement();

    public static double getXZAngleRadians(Vec3 vector) {
        return Math.atan2(vector.z, vector.x);
    }

}
