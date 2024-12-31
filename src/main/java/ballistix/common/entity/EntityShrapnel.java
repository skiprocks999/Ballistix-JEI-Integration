package ballistix.common.entity;

import java.util.List;

import javax.annotation.Nullable;

import ballistix.registers.BallistixDamageTypes;
import ballistix.registers.BallistixEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.AABB;

public class EntityShrapnel extends ThrowableProjectile {
    private static final EntityDataAccessor<Boolean> ISEXPLOSIVE = SynchedEntityData.defineId(EntityShrapnel.class, EntityDataSerializers.BOOLEAN);
    public boolean isExplosive = false;

    public EntityShrapnel(EntityType<? extends EntityShrapnel> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityShrapnel(Level worldIn) {
        this(BallistixEntities.ENTITY_SHRAPNEL.get(), worldIn);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            entityData.set(ISEXPLOSIVE, isExplosive);
        } else {
            isExplosive = entityData.get(ISEXPLOSIVE);
        }
        if (!isNoGravity()) {
            this.setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        setPos(getX() + getDeltaMovement().x, getY() + getDeltaMovement().y, getZ() + getDeltaMovement().z);
        EntityDimensions size = getDimensions(Pose.STANDING);
        setBoundingBox(new AABB(getX() - size.width() * 2, getY() - size.height() * 2, getZ() - size.width() * 2, getX() + size.width() * 2, getY() + size.height() * 2, getZ() + size.width() * 2));
        if (onGround() || tickCount > (isExplosive ? 400 : 100) || level().getBlockState(blockPosition()).blocksMotion()) {
            remove(RemovalReason.DISCARDED);
        }
        if (!level().isClientSide) {
            List<LivingEntity> livings = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox());
            for (LivingEntity living : livings) {
                living.hurt(living.damageSources().source(BallistixDamageTypes.SHRAPNEL), 10);
                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void shootFromRotation(@Nullable Entity entity, float pitch, float yaw, float par4, float force, float par6) {
        float f = -Mth.sin(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
        float f1 = -Mth.sin((pitch + par4) * ((float) Math.PI / 180F));
        float f2 = Mth.cos(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
        shoot(f, f1, f2, force, par6);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("type", isExplosive);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ISEXPLOSIVE, false);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (isExplosive) {
            level().explode(this, level().damageSources().source(BallistixDamageTypes.SHRAPNEL), null, getX(), getY(), getZ(), 3, true, ExplosionInteraction.BLOCK);
        }
        super.remove(reason);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        isExplosive = compound.getBoolean("type");
    }

}
