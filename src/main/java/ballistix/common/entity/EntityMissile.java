package ballistix.common.entity;

import java.util.UUID;

import ballistix.api.missile.MissileManager;
import ballistix.api.missile.virtual.VirtualMissile;
import ballistix.registers.BallistixEntities;
import electrodynamics.Electrodynamics;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class EntityMissile extends Entity {

    private static final EntityDataAccessor<Integer> MISSILE_TYPE = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> POSITION = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DELTAMOVE = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.FLOAT);

    public int missileType = -1;
    public float speed = 0.0F;
    @Nullable
    public UUID id;

    public EntityMissile(EntityType<? extends EntityMissile> type, Level worldIn) {
        super(type, worldIn);
        blocksBuilding = true;
    }

    public EntityMissile(Level worldIn) {
        this(BallistixEntities.ENTITY_MISSILE.get(), worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(POSITION, new Vector3f(0, 0, 0));
        builder.define(MISSILE_TYPE, -1);
        builder.define(DELTAMOVE, new Vector3f(0, 0, 0));
        builder.define(SPEED, 0.0F);
    }

    @Override
    public void tick() {

        Level level = level();
        boolean isClientSide = level.isClientSide;
        boolean isServerSide = !isClientSide;

        if (isServerSide) {

            entityData.set(POSITION, new Vector3f((float) getX(), (float) getY(), (float) getZ()));
            entityData.set(MISSILE_TYPE, missileType);
            entityData.set(DELTAMOVE, new Vector3f((float) getDeltaMovement().x, (float) getDeltaMovement().y, (float) getDeltaMovement().z));
            entityData.set(SPEED, speed);

        } else {

            Vector3f pos = entityData.get(POSITION);
            setPos(pos.x, pos.y, pos.z);
            missileType = entityData.get(MISSILE_TYPE);
            Vector3f deltaMovement = entityData.get(DELTAMOVE);
            setDeltaMovement(deltaMovement.x, deltaMovement.y, deltaMovement.z);
            speed = entityData.get(SPEED);
        }

        if(isServerSide) {
            if(id == null) {
                removeAfterChangingDimensions();
                return;
            }

            VirtualMissile missile = MissileManager.getMissile(level.dimension(), id);

            if(missile == null) {
                removeAfterChangingDimensions();
                return;
            }

            if(missile.hasExploded()) {
                removeAfterChangingDimensions();
                return;
            }

            setPos(missile.position);
            setDeltaMovement(missile.deltaMovement);
            speed = missile.speed;

        }

        if (getDeltaMovement().length() > 0) {

            setXRot((float) (Math.atan(getDeltaMovement().y() / Math.sqrt(getDeltaMovement().x() * getDeltaMovement().x() + getDeltaMovement().z() * getDeltaMovement().z())) * 180.0D / Math.PI));
            setYRot((float) (Math.atan2(getDeltaMovement().x(), getDeltaMovement().z()) * 180.0D / Math.PI));

        }

        if (isServerSide || speed >= 3.0F) {
            return;
        }


        //exhaust only when missile is accelerating


        float widthOver2 = getDimensions(getPose()).width() / 2.0F;

        for (int i = 0; i < 5; i++) {

            float x = (float) (getX() - widthOver2 + Electrodynamics.RANDOM.nextFloat(widthOver2));
            float y = (float) (getY() - Electrodynamics.RANDOM.nextFloat(0.5F));
            float z = (float) (getZ() - widthOver2 + Electrodynamics.RANDOM.nextFloat(widthOver2));

            level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, -speed * (getDeltaMovement().x + Electrodynamics.RANDOM.nextFloat()), -speed * (getDeltaMovement().y - 0.075f + Electrodynamics.RANDOM.nextFloat()), -speed * (getDeltaMovement().z + Electrodynamics.RANDOM.nextFloat()));

        }

        float motionX = (float) (-speed * getDeltaMovement().x);
        float motionY = (float) (-speed * getDeltaMovement().y);
        float motionZ = (float) (-speed * getDeltaMovement().z);
        for (int i = 0; i < 4; i++) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.getX(), this.getY(), this.getZ(), random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
        }

        for (int i = 0; i < 4; i++) {
            level.addParticle(ParticleTypes.CLOUD, false, this.getX(), this.getY(), this.getZ(), random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
        }


    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return true;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.encode(new Vec3(getX(), getY(), getZ()), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("position", tag));
        compound.putInt("range", missileType);
        Vec3.CODEC.encode(getDeltaMovement(), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("movement", tag));
        if(id != null) {
            UUIDUtil.CODEC.encode(id, NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("id", tag));
        }

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("position")).ifSuccess(pair -> setPos(pair.getFirst()));
        missileType = compound.getInt("range");
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("movement")).ifSuccess(pair -> setDeltaMovement(pair.getFirst()));
        UUIDUtil.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("id")).ifSuccess(pair -> id = pair.getFirst());
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

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        if(!level().isClientSide && (reason == RemovalReason.UNLOADED_TO_CHUNK || reason == RemovalReason.UNLOADED_WITH_PLAYER) && id != null) {
            VirtualMissile missile = MissileManager.getMissile(level().dimension(), id);
            missile.setSpawned(false);
        }
        super.remove(reason);
    }

}
