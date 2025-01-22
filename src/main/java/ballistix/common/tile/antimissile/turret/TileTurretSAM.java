package ballistix.common.tile.antimissile.turret;

import ballistix.common.inventory.container.ContainerSAMTurret;
import ballistix.common.settings.Constants;
import ballistix.registers.BallistixItems;
import ballistix.registers.BallistixTiles;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TileTurretSAM extends GenericTileAMTurret {

    private int cooldown = 0;

    public TileTurretSAM(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_SAMTURRET.get(), worldPos, blockState, Constants.SAM_TURRET_USAGEPERTICK, Constants.SAM_TURRET_RANGE, Constants.SAM_TURRET_ROTATIONSPEEDRADIANS);
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(1)).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.values()).valid((integer, stack, componentInventory) -> stack.is(BallistixItems.ITEM_AAMISSILE)));
        addComponent(new ComponentContainerProvider("container.samturret", this).createMenu((id, player) -> new ContainerSAMTurret(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    @Override
    public void fireTickServer() {

        if(cooldown > 0) {
            cooldown--;
            return;
        }

        ComponentInventory inv = getComponent(IComponentType.Inventory);

        ItemStack missile = inv.getItem(0);

        if(missile.isEmpty()) {
            return;
        }

        // code here to fire missile



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
