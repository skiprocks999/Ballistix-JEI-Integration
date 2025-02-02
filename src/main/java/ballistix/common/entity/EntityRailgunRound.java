package ballistix.common.entity;

import ballistix.registers.BallistixEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityRailgunRound extends Entity {

    public EntityRailgunRound(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityRailgunRound(Level level) {
        this(BallistixEntities.ENTITY_RAILGUNROUND.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
