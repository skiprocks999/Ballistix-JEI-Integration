package ballistix.common.tile.turret.antimissile;

import ballistix.api.turret.ITarget;
import ballistix.common.entity.EntityBullet;
import ballistix.common.inventory.container.ContainerCIWSTurret;
import ballistix.common.settings.Constants;
import ballistix.common.tile.turret.antimissile.util.TileTurretAntimissileProjectile;
import ballistix.registers.BallistixSounds;
import ballistix.registers.BallistixTiles;
import com.mojang.datafixers.util.Pair;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TileTurretCIWS extends TileTurretAntimissileProjectile implements ITickableSound {

    public final Property<Boolean> outOfAmmo = property(new Property<>(PropertyTypes.BOOLEAN, "noammo", false));
    public final Property<Boolean> firing = property(new Property<>(PropertyTypes.BOOLEAN, "isfiring", false));
    public final Property<Boolean> targetingEntity = property(new Property<>(PropertyTypes.BOOLEAN, "targetingentity", false));

    private boolean isPlaying = false;
    private LivingEntity livingTarget = null;

    public TileTurretCIWS(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_CIWSTURRET.get(), worldPos, blockState, Constants.CIWS_TURRET_BASE_RANGE, 0, Constants.CIWS_TURRET_USAGEPERTICK, Constants.CIWS_TURRET_ROTATIONSPEEDRADIANS, Constants.CIWS_INNACCURACY);
    }

    @Override
    public ComponentInventory getInventory() {
        return new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(2).upgrades(3)).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.values());
    }

    @Override
    public ComponentContainerProvider getContainer() {
        return new ComponentContainerProvider("container.ciwsturret", this).createMenu((id, player) -> new ContainerCIWSTurret(id, player, getComponent(IComponentType.Inventory), getCoordsArray()));
    }

    @Override
    public void tickServerActive(ComponentTickable tickable) {
        if(!canFire) {
            firing.set(false);
        }
    }

    @Override
    public void fireTickServer(long ticks) {

        ComponentInventory inv = getComponent(IComponentType.Inventory);

        ItemStack missile = inv.getItem(0);

        if (missile.isEmpty()) {
            outOfAmmo.set(true);
            firing.set(false);
            //return;
        }

        outOfAmmo.set(false);
        firing.set(true);

        EntityBullet bullet = new EntityBullet(getLevel());

        bullet.speed = getProjectileSpeed();

        Pair<Vec3, Vec3> projectileVals = getProjectileTrajectoryFromInaccuracy(inaccuracy, baseRange, inaccuracyMultiplier.get(), getProjectileLaunchPosition(), getTargetPosition(getTarget(ticks)));

        Vec3 rotvec = projectileVals.getSecond();
        bullet.rotation = new Vector3f((float) rotvec.x, (float) rotvec.y, (float) rotvec.z);

        bullet.range = currentRange.get().floatValue();

        bullet.setDeltaMovement(projectileVals.getFirst());

        bullet.setPos(getProjectileLaunchPosition());

        level.addFreshEntity(bullet);

        inv.removeItem(0, 1);

    }

    @Override
    public void tickClient(ComponentTickable tickable) {
        if(shouldPlaySound() && !isPlaying) {
            isPlaying = true;
            SoundBarrierMethods.playTileSound(BallistixSounds.SOUND_CIWS_TURRETFIRING.get(), SoundSource.BLOCKS, this, 1.0F, 1.0F, true);
        }
    }

    @Override
    public Vec3 getProjectileLaunchPosition() {
        BlockPos above = getBlockPos();
        return new Vec3(above.getX() + 0.5, above.getY() + 0.8, above.getZ() + 0.5);
    }

    @Override
    public double getMinElevation() {
        return -0.5;
    }

    @Override
    public double getMaxElevation() {
        return 1;
    }

    @Override
    public float getProjectileSpeed() {
        return 5.0F;
    }

    @Override
    public void setNotPlaying() {
        isPlaying = false;
    }

    @Override
    public boolean shouldPlaySound() {
        return firing.get();
    }

    @Nullable
    @Override
    public ITarget getTarget(long ticks) {

        targetingEntity.set(false);

        ITarget target = super.getTarget(ticks);

        if(target != null) {
            return target;
        }

        if(livingTarget != null && (livingTarget.isRemoved() || livingTarget.isDeadOrDying())) {
            livingTarget = null;
        }

        if(ticks % 5 == 0) {

            LivingEntity selected = null;
            double lastMag = 0;

            for(LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos()).inflate(currentRange.get() / 4.0))) {
                if(raycastToBlockPos(level, getBlockPos(), entity.blockPosition().above()).isEmpty() && !(entity instanceof Player player && player.isCreative()) && !entity.isDeadOrDying() && !entity.isRemoved()) {
                    double deltaX = entity.getX() - getBlockPos().getX();
                    double deltaY = entity.getY() - getBlockPos().getY();
                    double deltaZ = entity.getZ() - getBlockPos().getZ();

                    double mag = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                    if(selected == null) {
                        selected = entity;
                        lastMag = mag;
                    } else if(mag < lastMag){
                        selected = entity;
                    }
                }
            }

            livingTarget = selected;
        }

        if(livingTarget != null) {
            targetingEntity.set(true);
            return new ITarget.TargetLivingEntity(livingTarget);
        }

        return null;
    }

    @Override
    public boolean isValidPlacement() {
        return !targetingEntity.get() || super.isValidPlacement();
    }

    public static List<Block> raycastToBlockPos(Level world, BlockPos start, BlockPos end) {

        List<Block> blocks = new ArrayList<>();

        int deltaX = end.getX() - start.getX();
        int deltaY = end.getY() - start.getY();
        int deltaZ = end.getZ() - start.getZ();

        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        int maxChecks = (int) magnitude;

        double incX = deltaX / magnitude;
        double incY = deltaY / magnitude;
        double incZ = deltaZ / magnitude;

        double x = 0;
        double y = 0;
        double z = 0;

        BlockPos toCheck = start;
        BlockState state;

        int i = 0;

        while (i < maxChecks) {

            x += incX;
            y += incY;
            z += incZ;
            toCheck = new BlockPos((int)(start.getX() + x), (int) Math.ceil(start.getY() + y), (int) (start.getZ() + z));
            if (!toCheck.equals(start) && !toCheck.equals(end)) {
                state = world.getBlockState(toCheck);
                if(!state.isAir() && !state.isCollisionShapeFullBlock(world, toCheck)) {
                    blocks.add(state.getBlock());
                }
                //world.setBlockAndUpdate(toCheck, Blocks.COBBLESTONE.defaultBlockState());
            }

            i++;

        }

        return blocks;
    }
}
