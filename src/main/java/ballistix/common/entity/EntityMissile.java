package ballistix.common.entity;

import ballistix.common.blast.Blast;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.registers.BallistixEntities;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class EntityMissile extends Entity {
	
	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> RANGE = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ISSTUCK = SynchedEntityData.defineId(EntityMissile.class, EntityDataSerializers.INT);
	public BlockPos target = BlockEntityUtils.OUT_OF_REACH;
	public int blastOrdinal = -1;
	public int range = -1;
	public boolean isItem = false;
	private EntityBlast blastEntity = null;
	public boolean isStuck = false;

	public EntityMissile(EntityType<? extends EntityMissile> type, Level worldIn) {
		super(type, worldIn);
		blocksBuilding = true;
	}

	public EntityMissile(Level worldIn) {
		this(BallistixEntities.ENTITY_MISSILE.get(), worldIn);
	}

	public void setBlastType(SubtypeBlast explosive) {
		blastOrdinal = explosive.ordinal();
	}

	public SubtypeBlast getBlastType() {
		return blastOrdinal == -1 ? null : SubtypeBlast.values()[blastOrdinal];
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().expandTowards(20, 20, 20);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(TYPE, -1);
		builder.define(RANGE, -1);
		builder.define(ISSTUCK, -1);
	}

	@Override
	public void tick() {
		if (isStuck) {
			if (!level().isClientSide && blastEntity.getBlast().hasStarted) {
				removeAfterChangingDimensions();
			}
			return;
		}
		BlockState state = level().getBlockState(blockPosition());
		if (!level().isClientSide || state.getCollisionShape(level(), blockPosition()).isEmpty()) {
			setPos(getX() + getDeltaMovement().x, getY() + getDeltaMovement().y, getZ() + getDeltaMovement().z);
		}
		if (blastEntity == null) {
			if (getDeltaMovement().length() > 0 && !isStuck) {
				setXRot((float) (Math.atan(getDeltaMovement().y() / Math.sqrt(getDeltaMovement().x() * getDeltaMovement().x() + getDeltaMovement().z() * getDeltaMovement().z())) * 180.0D / Math.PI));
				setYRot((float) (Math.atan2(getDeltaMovement().x(), getDeltaMovement().z()) * 180.0D / Math.PI));
			}
			if (!level().isClientSide) {
				if (!state.getCollisionShape(level(), blockPosition()).isEmpty() || !isItem && !target.equals(BlockEntityUtils.OUT_OF_REACH) && getY() < target.getY() && getDeltaMovement().y() < 0) {
					if (blastOrdinal != -1 && (target.equals(BlockEntityUtils.OUT_OF_REACH) || tickCount > 20)) {
						SubtypeBlast explosive = SubtypeBlast.values()[blastOrdinal];
						setPos(getX() - getDeltaMovement().x * 2, getY() - getDeltaMovement().y * 2, getZ() - getDeltaMovement().z * 2);
						Blast b = explosive.createBlast(level(), blockPosition());
						if (b != null) {
							blastEntity = b.performExplosion();
							if (blastEntity == null) {
								removeAfterChangingDimensions();
							} else {
								isStuck = true;
							}
						}
					}
				}
				if (!isItem && getY() > 500) {
					if (target.equals(BlockEntityUtils.OUT_OF_REACH)) {
						removeAfterChangingDimensions();
					} else {
						setPos(target.getX(), 499, target.getZ());
						setDeltaMovement(0, -3f, 0);
					}
				}
			}
			if (!level().isClientSide) {
				entityData.set(TYPE, blastOrdinal);
				entityData.set(RANGE, range);
				entityData.set(ISSTUCK, isStuck ? 1 : -1);
			} else {
				blastOrdinal = entityData.get(TYPE);
				range = entityData.get(RANGE);
				boolean old = isStuck;
				isStuck = entityData.get(ISSTUCK) > 0;
				if (isStuck != old) {
					setPos(getX() - getDeltaMovement().x * 1, getY() - getDeltaMovement().y * 1, getZ() - getDeltaMovement().z * 1);
				}
			}
			if (!isItem && !target.equals(BlockEntityUtils.OUT_OF_REACH) && getDeltaMovement().y < 3 && getDeltaMovement().y >= 0) {
				setDeltaMovement(getDeltaMovement().add(0, 0.02, 0));
			}
			for (int i = 0; i < 5; i++) {
				float str = level().random.nextFloat() * 0.25f;
				float ranX = str * (level().random.nextFloat() - 0.5f);
				float ranY = str * (level().random.nextFloat() - 0.5f);
				float ranZ = str * (level().random.nextFloat() - 0.5f);
				float x = (float) (getX() - getDimensions(getPose()).width() / 1.0f);
				float y = (float) (getY() + getDimensions(getPose()).height() / 1.0f);
				float z = (float) (getZ() - getDimensions(getPose()).width() / 1.0f);
				level().addParticle(ParticleTypes.LARGE_SMOKE, x - 0.5 + ranX, y + ranY, z - 0.5 + ranZ, -getDeltaMovement().x + ranX, -getDeltaMovement().y - 0.075f + ranY, -getDeltaMovement().z + ranZ);

			}
			float motionX = (float) -getDeltaMovement().x;
			float motionY = (float) -getDeltaMovement().y;
			float motionZ = (float) -getDeltaMovement().z;
			for (int i = 0; i < 4; i++) {
				level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.getX() - 0.5, this.getY(), this.getZ() - 0.5, random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
			}
			for (int i = 0; i < 4; i++) {
				level().addParticle(ParticleTypes.CLOUD, false, this.getX() - 0.5, this.getY(), this.getZ() - 0.5, random.nextDouble() / 1.5 - 0.3333 + motionX, random.nextDouble() / 1.5 - 0.3333 + motionY, random.nextDouble() / 1.5 - 0.3333 + motionZ);
			}
		}
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return true;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("type", blastOrdinal);
		compound.putInt("range", range);
		compound.putBoolean("isItem", isItem);
		compound.put("target", NbtUtils.writeBlockPos(target));

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		blastOrdinal = compound.getInt("type");
		range = compound.getInt("range");
		isItem = compound.getBoolean("isItem");
		if (blastOrdinal != -1) {
			setBlastType(getBlastType());
		}
		Optional<BlockPos> pos = NbtUtils.readBlockPos(compound, "target");
        target = pos.orElse(BlockEntityUtils.OUT_OF_REACH);
	}
	
}
