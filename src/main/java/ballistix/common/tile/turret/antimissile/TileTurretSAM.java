package ballistix.common.tile.turret.antimissile;

import org.joml.Vector3f;

import com.mojang.datafixers.util.Pair;

import ballistix.common.entity.EntitySAM;
import ballistix.common.inventory.container.ContainerSAMTurret;
import ballistix.common.settings.Constants;
import ballistix.common.tile.turret.antimissile.util.TileTurretAntimissileProjectile;
import ballistix.registers.BallistixItems;
import ballistix.registers.BallistixSounds;
import ballistix.registers.BallistixTiles;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TileTurretSAM extends TileTurretAntimissileProjectile {

    public final Property<Integer> cooldown = property(new Property<>(PropertyTypes.INTEGER, "cooldown", 0));
    public final Property<Boolean> outOfAmmo = property(new Property<>(PropertyTypes.BOOLEAN, "noammo", false));

    public TileTurretSAM(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_SAMTURRET.get(), worldPos, blockState, Constants.SAM_TURRET_BASE_RANGE, 100, Constants.SAM_TURRET_USAGEPERTICK, Constants.SAM_TURRET_ROTATIONSPEEDRADIANS, Constants.SAM_INNACCURACY);
    }

    @Override
    public ComponentInventory getInventory() {
        return new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(1).upgrades(3)).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.values()).valid((index, stack, inv) -> {

            if (index == 0) {
                return stack.is(BallistixItems.ITEM_AAMISSILE);
            } else if (index >= inv.getUpgradeSlotStartIndex()) {
                return stack.getItem() instanceof ItemUpgrade upgrade && inv.isUpgradeValid(upgrade.subtype);
            } else {
                return false;
            }

        });
    }

    @Override
    public ComponentContainerProvider getContainer() {
        return new ComponentContainerProvider("container.samturret", this).createMenu((id, player) -> new ContainerSAMTurret(id, player, getComponent(IComponentType.Inventory), getCoordsArray()));
    }

    @Override
    public void tickServerActive(ComponentTickable tickable) {
        if (cooldown.get() > 0) {
            cooldown.set(cooldown.get() - 1);
        }
    }

    @Override
    public void fireTickServer(long ticks) {

        if (cooldown.get() > 0) {
            return;
        }

        ComponentInventory inv = getComponent(IComponentType.Inventory);

        ItemStack missile = inv.getItem(0);

        if (missile.isEmpty()) {
            outOfAmmo.set(true);
            return;
        }

        outOfAmmo.set(false);

        EntitySAM sam = new EntitySAM(getLevel());

        sam.speed = getProjectileSpeed();

        Pair<Vec3, Vec3> projectileVals = getProjectileTrajectoryFromInaccuracy(inaccuracy, baseRange, inaccuracyMultiplier.get(), getProjectileLaunchPosition(), getTargetPosition(getTarget(ticks)));

        Vec3 rotvec = projectileVals.getSecond();
        sam.rotation = new Vector3f((float) rotvec.x, (float) rotvec.y, (float) rotvec.z);

        sam.range = currentRange.get().floatValue();

        sam.setDeltaMovement(projectileVals.getFirst());

        sam.setPos(getProjectileLaunchPosition());

        level.addFreshEntity(sam);

        level.playSound(null, getBlockPos().above(), BallistixSounds.SOUND_MISSILE_ROCKETLAUNCHER.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

        cooldown.set(Constants.SAM_TURRET_COOLDOWN);

        inv.removeItem(0, 1);

    }

    @Override
    public Vec3 getProjectileLaunchPosition() {
        BlockPos above = getBlockPos().above();
        return new Vec3(above.getX() + 0.5, above.getY() + 0.5, above.getZ() + 0.5);
    }

    @Override
    public float getProjectileSpeed() {
        return 3.0F;
    }

    @Override
    public double getMinElevation() {
        return -0.5;
    }

    @Override
    public double getMaxElevation() {
        return 1;
    }

}
