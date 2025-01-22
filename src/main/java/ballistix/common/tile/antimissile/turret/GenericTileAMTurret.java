package ballistix.common.tile.antimissile.turret;

import ballistix.common.tile.antimissile.TileFireControlRadar;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class GenericTileAMTurret extends GenericTile {

    private BlockPos boundFireControl = BlockEntityUtils.OUT_OF_REACH;
    @Nullable
    private TileFireControlRadar radar;
    public boolean hasTarget = false;
    private final double range;
    private final double rotationSpeedRadians;
    private final double usage;
    boolean canFire = false;

    public final Property<Vec3> turretRotation = property(new Property<>(PropertyTypes.VEC3, "turrot", getDefaultOrientation()));
    public final Property<Vec3> desiredRotation = property(new Property<>(PropertyTypes.VEC3, "currot", getDefaultOrientation()));

    public GenericTileAMTurret(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState, double usage, double range, double rotationSpeedRadians) {
        super(tileEntityTypeIn, worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(usage * 20));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        this.usage = usage;
        this.range = range;
        this.rotationSpeedRadians = rotationSpeedRadians;
    }

    public void tickServer(ComponentTickable tickable) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (electro.getJoulesStored() < usage || level.getBrightness(LightLayer.SKY, getBlockPos()) <= 0) {
            return;
        }

        if (tickable.getTicks() % 10 == 0 && radar == null) {
            if (level.getBlockEntity(boundFireControl) instanceof TileFireControlRadar fire) {
                radar = fire;
            }
        }
        hasTarget = radar != null && radar.tracking != null && !radar.tracking.isRemoved();

        canFire = false;

        double distanceToTarget = 0;

        if (hasTarget) {

            float trackingSpeed = 2;//radar.tracking.speed;
            Vec3 trackingVector = radar.tracking.getDeltaMovement();


            double timeToIntercept = TileFireControlRadar.getTimeToIntercept(radar.tracking.getPosition(), trackingVector, trackingSpeed, getProjectileSpeed(), getProjectileLaunchPosition());

            if (timeToIntercept >= 0) {

                Vec3 interceptPos = radar.tracking.getPosition().add(trackingVector.scale(trackingSpeed).scale(timeToIntercept));

                Vec3 launchPos = getProjectileLaunchPosition();

                double deltaX = interceptPos.x - launchPos.x;
                double deltaY = interceptPos.y - launchPos.y;
                double deltaZ = interceptPos.z - launchPos.z;

                double magXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                if(magXZ <= 0) {
                    magXZ = 1;
                }

                deltaY = deltaY - (int) deltaY; //only leaves decimals

                desiredRotation.set(new Vec3(deltaX / magXZ, deltaY, deltaZ / magXZ));

                distanceToTarget = TileFireControlRadar.getDistanceToMissile(getProjectileLaunchPosition(), interceptPos);

            }

        } else {
            desiredRotation.set(getDefaultOrientation());
        }

        if (turretRotation.get().equals(desiredRotation.get())) {

            canFire = hasTarget && distanceToTarget > 0 && distanceToTarget <= range;

        } else {

            double thetaDesiredXZ = getXZAngleRadians(desiredRotation.get());
            double thetaCurrXZ = getXZAngleRadians(turretRotation.get());

            double angleDifXZ = thetaDesiredXZ - thetaCurrXZ;

            double deltaY = desiredRotation.get().y - turretRotation.get().y;

            if (deltaY < 0) {
                turretRotation.set(turretRotation.get().add(0, -Math.cos(rotationSpeedRadians) * 0.5, 0));
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

            canFire = hasTarget && turretRotation.get().equals(desiredRotation.get()) && distanceToTarget > 0 && distanceToTarget <= range;

        }

        if (canFire) {
            fireTickServer();
        }


    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("bound", NbtUtils.writeBlockPos(boundFireControl));
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        boundFireControl = NbtUtils.readBlockPos(compound, "bound").orElse(BlockEntityUtils.OUT_OF_REACH);
    }

    public abstract void fireTickServer();

    public void bindFireControlRadar(BlockPos pos) {
        boundFireControl = pos;
    }

    public Vec3 getDefaultOrientation() {
        Direction facing = getFacing();
        if(facing == Direction.NORTH) {
            //facing = Direction.SOUTH;
        }
        double mag = Math.sqrt(facing.getStepX() * facing.getStepX() + facing.getStepZ() * facing.getStepZ());
        if(mag <= 0) {
            mag = 1;
        }
        return new Vec3(facing.getStepX() / mag, 0, facing.getStepZ() / mag);
    }

    public abstract Vec3 getProjectileLaunchPosition();

    // speed in units of ticks
    public abstract float getProjectileSpeed();

    public abstract double getMinElevation();

    public abstract double getMaxElevation();

    public static double getXZAngleRadians(Vec3 vector) {
        return Math.abs(Math.atan2(vector.z, vector.x));
    }
}
