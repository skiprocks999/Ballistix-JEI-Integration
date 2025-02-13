package ballistix.common.entity;

import java.util.UUID;

import ballistix.api.missile.MissileManager;
import ballistix.api.missile.virtual.VirtualProjectile;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.NbtOps;
import org.joml.Vector3f;

import ballistix.registers.BallistixEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EntityRailgunRound extends Entity {

    private static final float RAD2DEG = (float) (180.0F / Math.PI);

    private static final EntityDataAccessor<Vector3f> ROTATION = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Float> SPEED = SynchedEntityData.defineId(EntityRailgunRound.class, EntityDataSerializers.FLOAT);


    public Vector3f rotation = new Vector3f(0, 0, 0);
    @Nullable
    public UUID id;
    public float speed = 0.0F;

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

        if (tickCount > 30 && getDeltaMovement().length() <= 0) {
            if (isServer) {
                removeAfterChangingDimensions();
            }
            return;
        }

        if (isServer) {
            if (id == null) {
                removeAfterChangingDimensions();
                return;
            }

            VirtualProjectile.VirtualRailgunRound railgunround = MissileManager.getRailgunRound(level.dimension(), id);

            if (railgunround == null) {
                removeAfterChangingDimensions();
                return;
            }

            if (railgunround.hasExploded()) {
                removeAfterChangingDimensions();
                return;
            }

            if(blockPosition().equals(railgunround.blockPosition())) {
                setPos(railgunround.position);
                setDeltaMovement(railgunround.deltaMovement);
                speed = railgunround.speed;
            }

        }

        if (isServer) {
            entityData.set(SPEED, speed);
            entityData.set(ROTATION, rotation);
        } else {
            speed = entityData.get(SPEED);
            rotation = entityData.get(ROTATION);
        }

        for (int i = 0; i < speed; i++) {

            setPos(new Vec3(getX() + getDeltaMovement().x, getY() + getDeltaMovement().y, getZ() + getDeltaMovement().z));

        }

        setYRot((float) Math.atan2(rotation.z, rotation.x) * RAD2DEG);
        setXRot((float) (Math.asin(rotation.y) * RAD2DEG));


    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPEED, 0.0F);
        builder.define(ROTATION, new Vector3f(0, 0, 0));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("position")).ifSuccess(pair -> setPos(pair.getFirst()));
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("movement")).ifSuccess(pair -> setDeltaMovement(pair.getFirst()));
        UUIDUtil.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("id")).ifSuccess(pair -> id = pair.getFirst());
        rotation = new Vector3f(compound.getFloat("xrot"), compound.getFloat("yrot"), compound.getFloat("zrot"));
        compound.putFloat("speed", speed);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.encode(new Vec3(getX(), getY(), getZ()), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("position", tag));
        Vec3.CODEC.encode(getDeltaMovement(), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("movement", tag));
        if(id != null) {
            UUIDUtil.CODEC.encode(id, NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("id", tag));
        }
        compound.putFloat("xrot", rotation.x);
        compound.putFloat("yrot", rotation.y);
        compound.putFloat("zrot", rotation.z);
        speed = compound.getFloat("speed");
    }

}
