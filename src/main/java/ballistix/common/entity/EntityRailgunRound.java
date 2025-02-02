package ballistix.common.entity;

import ballistix.common.settings.Constants;
import ballistix.registers.BallistixDamageTypes;
import ballistix.registers.BallistixEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashSet;

public class EntityRailgunRound extends Entity {

    private static final float RAD2DEG = (float) (180.0F / Math.PI);

    private static final EntityDataAccessor<Float> DISTANCE_TRAVELED = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> ROTATION = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.FLOAT);

    public float speed = 0F;
    private float distanceTraveled = 0;
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float range = (float) Constants.CIWS_TURRET_BASE_RANGE;
    int damage = 100;

    public EntityRailgunRound(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityRailgunRound(Level level) {
        this(BallistixEntities.ENTITY_RAILGUNROUND.get(), level);
    }

    @Override
    public void tick() {
        Level level = level();

        boolean isClient = level.isClientSide();

        boolean isServer = !isClient;

        if(isServer) {
            entityData.set(DISTANCE_TRAVELED, distanceTraveled);
            entityData.set(SPEED, speed);
            entityData.set(ROTATION, rotation);
            entityData.set(RANGE, range);
        } else {
            distanceTraveled = entityData.get(DISTANCE_TRAVELED);
            speed = entityData.get(SPEED);
            rotation = entityData.get(ROTATION);
            range = entityData.get(RANGE);
        }

        if(tickCount > 30 && getDeltaMovement().length() <= 0) {
            if(isServer) {
                removeAfterChangingDimensions();
            }
            return;
        }


        if(distanceTraveled >= range + 5) {
            if(isServer) {
                removeAfterChangingDimensions();
            }
            return;
        }

        Vec3 movement = getDeltaMovement();

        setPos(getX() + movement.x * speed, getY() + movement.y * speed, getZ() + movement.z * speed);

        setYRot((float) Math.atan2(rotation.z, rotation.x) * RAD2DEG);
        setXRot((float) (Math.asin(rotation.y) * RAD2DEG));

        BlockState state = level.getBlockState(blockPosition());

        if(!state.getCollisionShape(level, blockPosition()).isEmpty() && tickCount > 30) {
            if(isServer) {
                level.destroyBlock(blockPosition(), false);
                removeAfterChangingDimensions();
            }
            return;
        }

        distanceTraveled += speed;

        if(isServer) {

            AABB box = getBoundingBox().inflate(speed);

            for(EntityMissile missile : EntityMissile.MISSILES.getOrDefault(level.dimension(), new HashSet<>())) {

                if(!missile.isRemoved() && missile.getBoundingBox().intersects(box)) {
                    level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 2.0F, 1.0F);

                    missile.remove(RemovalReason.CHANGED_DIMENSION);

                    removeAfterChangingDimensions();
                    return;
                }

            }

            LivingEntity selected = null;
            double lastMag = 0;

            for(LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box)) {

                double deltaX = entity.getX() - getX();
                double deltaY = entity.getY() - getY();
                double deltaZ = entity.getZ() - getZ();

                double mag = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if(selected == null) {
                    selected = entity;
                    lastMag = mag;
                } else if(mag < lastMag){
                    selected = entity;
                }

            }

            if(selected != null) {
                selected.hurt(selected.damageSources().source(BallistixDamageTypes.CIWS_BULLET), 10);
                removeAfterChangingDimensions();
            }

        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DISTANCE_TRAVELED, 0.0F);
        builder.define(SPEED, 0.0F);
        builder.define(ROTATION, new Vector3f(0, 0, 0));
        builder.define(RANGE, (float) Constants.SAM_TURRET_BASE_RANGE);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("speed", speed);
        compound.putFloat("distance", distanceTraveled);
        compound.putFloat("xrot", rotation.x);
        compound.putFloat("yrot", rotation.y);
        compound.putFloat("zrot", rotation.z);
        compound.putFloat("range", range);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        speed = compound.getFloat("speed");
        distanceTraveled = compound.getFloat("distance");
        rotation = new Vector3f(compound.getFloat("xrot"), compound.getFloat("yrot"), compound.getFloat("zrot"));
        range = compound.getFloat("range");
    }

}
