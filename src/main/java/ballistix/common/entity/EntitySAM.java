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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

//want to keep this separate from the missiles since this thing has one job and one job only :D
public class EntitySAM extends Entity {

    private static final float RAD2DEG = (float) (180.0F / Math.PI);

    private static final EntityDataAccessor<Vector3f> POSITION = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DELTAMOVE = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> ROTATION = SynchedEntityData.defineId(EntitySAM.class, EntityDataSerializers.VECTOR3);


    public Vector3f rotation = new Vector3f(0, 0, 0);
    @Nullable
    public UUID id;

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

        if (isServer) {
            entityData.set(POSITION, new Vector3f((float) getX(), (float) getY(), (float) getZ()));
            entityData.set(DELTAMOVE, new Vector3f((float) getDeltaMovement().x, (float) getDeltaMovement().y, (float) getDeltaMovement().z));
            entityData.set(ROTATION, rotation);
        } else {
            Vector3f pos = entityData.get(POSITION);
            setPos(pos.x, pos.y, pos.z);
            Vector3f deltaMovement = entityData.get(DELTAMOVE);
            setDeltaMovement(deltaMovement.x, deltaMovement.y, deltaMovement.z);
            rotation = entityData.get(ROTATION);
        }

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

            VirtualProjectile.VirtualSAM sam = MissileManager.getSAM(level.dimension(), id);

            if (sam == null) {
                removeAfterChangingDimensions();
                return;
            }

            if (sam.hasExploded()) {
                removeAfterChangingDimensions();
                return;
            }

            setPos(sam.position);
            setDeltaMovement(sam.deltaMovement);

        }

        setYRot((float) Math.atan2(rotation.z, rotation.x) * RAD2DEG);
        setXRot((float) (Math.asin(rotation.y) * RAD2DEG));


    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(POSITION, new Vector3f(0, 0, 0));
        builder.define(DELTAMOVE, new Vector3f(0, 0, 0));
        builder.define(ROTATION, new Vector3f(0, 0, 0));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("position")).ifSuccess(pair -> setPos(pair.getFirst()));
        Vec3.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("movement")).ifSuccess(pair -> setDeltaMovement(pair.getFirst()));
        UUIDUtil.CODEC.decode(NbtOps.INSTANCE, compound.getCompound("id")).ifSuccess(pair -> id = pair.getFirst());
        rotation = new Vector3f(compound.getFloat("xrot"), compound.getFloat("yrot"), compound.getFloat("zrot"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        Vec3.CODEC.encode(new Vec3(getX(), getY(), getZ()), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("position", tag));
        Vec3.CODEC.encode(getDeltaMovement(), NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("movement", tag));
        UUIDUtil.CODEC.encode(id, NbtOps.INSTANCE, new CompoundTag()).ifSuccess(tag -> compound.put("id", tag));
        compound.putFloat("xrot", rotation.x);
        compound.putFloat("yrot", rotation.y);
        compound.putFloat("zrot", rotation.z);
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
