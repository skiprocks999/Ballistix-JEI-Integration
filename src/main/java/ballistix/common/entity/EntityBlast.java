package ballistix.common.entity;

import ballistix.common.blast.Blast;
import ballistix.common.blast.IHasCustomRenderer;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.registers.BallistixEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityBlast extends Entity {
	private static final EntityDataAccessor<Integer> CALLCOUNT = SynchedEntityData.defineId(EntityBlast.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityBlast.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> SHOULDSTARTCUSTOMRENDER = SynchedEntityData.defineId(EntityBlast.class, EntityDataSerializers.BOOLEAN);

	private Blast blast;
	public int blastOrdinal = -1;
	public int callcount = 0;
	public boolean shouldRenderCustom = false;
	public int ticksWhenCustomRender;

	@Override
	public boolean shouldRender(double x, double y, double z) {
		return true;
	}

	public EntityBlast(EntityType<? extends EntityBlast> type, Level worldIn) {
		super(type, worldIn);
		blocksBuilding = true;
	}

	public EntityBlast(Level worldIn) {
		this(BallistixEntities.ENTITY_BLAST.get(), worldIn);
	}

	public void setBlastType(SubtypeBlast explosive) {
		blastOrdinal = explosive.ordinal();
		blast = getBlastType().createBlast(level(), blockPosition());
	}

	public SubtypeBlast getBlastType() {
		return blastOrdinal == -1 ? null : SubtypeBlast.values()[blastOrdinal];
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(CALLCOUNT, 80);
		builder.define(TYPE, -1);
		builder.define(SHOULDSTARTCUSTOMRENDER, false);
	}

	@Override
	public void tick() {
		if (!level().isClientSide) {
			entityData.set(TYPE, blastOrdinal);
			entityData.set(CALLCOUNT, callcount);
			entityData.set(SHOULDSTARTCUSTOMRENDER, blast instanceof IHasCustomRenderer has && has.shouldRender());
		} else {
			blastOrdinal = entityData.get(TYPE);
			callcount = entityData.get(CALLCOUNT);
			if (!shouldRenderCustom && entityData.get(SHOULDSTARTCUSTOMRENDER) == Boolean.TRUE) {
				ticksWhenCustomRender = tickCount;
			}
			shouldRenderCustom = entityData.get(SHOULDSTARTCUSTOMRENDER);
		}
		if (blast != null) {
			if (callcount == 0) {
				blast.preExplode();
			} else if (blast.explode(callcount)) {
				blast.postExplode();
				remove(RemovalReason.DISCARDED);
			}
			callcount++;
		} else if (blastOrdinal == -1) {
			if (tickCount > 60) {
				remove(RemovalReason.DISCARDED);
			}
		} else {
			blast = getBlastType().createBlast(level(), blockPosition());
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("type", blastOrdinal);
		compound.putInt("callcount", callcount);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		blastOrdinal = compound.getInt("type");
		callcount = compound.getInt("callcount");
		if (blastOrdinal != -1) {
			setBlastType(getBlastType());
		}
	}

	public Blast getBlast() {
		return blast;
	}
}
