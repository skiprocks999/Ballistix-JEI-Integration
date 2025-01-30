package ballistix.common.entity;

import ballistix.common.settings.Constants;
import ballistix.registers.BallistixEntities;
import electrodynamics.Electrodynamics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashSet;

//want to keep this separate from the missiles since this thing has one job and one job only :D
public class EntitySAM extends Entity {

    private static final float RAD2DEG = (float) (180.0F / Math.PI);

    private static final EntityDataAccessor<Float> DISTANCE_TRAVELED = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> ROTATION = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.FLOAT);

    public float speed = 0F;
    private float distanceTraveled = 0;
    public Vector3f rotation = new Vector3f(0, 0, 0);
    public float range = (float) Constants.SAM_TURRET_BASE_RANGE;

    public EntitySAM(Level level) {
        this(BallistixEntities.ENTITY_SAM.get(), level);
    }

    public EntitySAM(EntityType<?> entityType, Level level) {
        super(entityType, level);
        blocksBuilding = true;
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

        if(distanceTraveled >= range + 5) {
            if(isServer) {
                level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 2.0F, 1.0F);
                removeAfterChangingDimensions();
            }
            return;
        }

        Vec3 movement = getDeltaMovement();

        setPos(getX() + movement.x * speed, getY() + movement.y * speed, getZ() + movement.z * speed);

        setYRot((float) Math.atan2(rotation.z, rotation.x) * RAD2DEG);
        setXRot((float) (Math.asin(rotation.y) * RAD2DEG));

        BlockState state = level.getBlockState(blockPosition());

        if(!state.getCollisionShape(level, blockPosition()).isEmpty() && tickCount > 5) {
            if(isServer) {
                level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 2.0F, 1.0F);
                removeAfterChangingDimensions();
            }
            return;
        }

        distanceTraveled += speed;

        if(isServer) {

            for(EntityMissile missile : EntityMissile.MISSILES.getOrDefault(level.dimension(), new HashSet<>())) {

                if(!missile.isRemoved() && missile.getBoundingBox().intersects(getBoundingBox().inflate(speed))) {
                    detonate(missile);
                    return;
                }

            }

        } else if(tickCount < 20) {

            float widthOver2 = getDimensions(getPose()).width() / 2.0F;

            for (int i = 0; i < 1; i++) {

                float x = (float) (getX() - widthOver2 + Electrodynamics.RANDOM.nextFloat(widthOver2));
                float y = (float) (getY() - Electrodynamics.RANDOM.nextFloat(0.5F));
                float z = (float) (getZ() - widthOver2 + Electrodynamics.RANDOM.nextFloat(widthOver2));

                level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, -speed * (getDeltaMovement().x + Electrodynamics.RANDOM.nextFloat()), -speed * (getDeltaMovement().y - 0.075f + Electrodynamics.RANDOM.nextFloat()), -speed * (getDeltaMovement().z + Electrodynamics.RANDOM.nextFloat()));

            }

            float motionX = (float) (-speed * getDeltaMovement().x);
            float motionY = (float) (-speed * getDeltaMovement().y);
            float motionZ = (float) (-speed * getDeltaMovement().z);
            for (int i = 0; i < 1; i++) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.getX(), this.getY(), this.getZ(), random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
            }

            for (int i = 0; i < 1; i++) {
                level.addParticle(ParticleTypes.CLOUD, false, this.getX(), this.getY(), this.getZ(), random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
            }
        }

    }

    // serverside only
    public void detonate(EntityMissile missile) {

        level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 2.0F, 1.0F);

        missile.remove(RemovalReason.CHANGED_DIMENSION);

        removeAfterChangingDimensions();

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

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return true;
    }

    @Override
    public void checkDespawn() {

    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (!this.level().isClientSide) {
                return player.startRiding(this, true) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

}
