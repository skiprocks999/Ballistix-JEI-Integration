package ballistix.common.inventory.container;

import ballistix.common.tile.turret.antimissile.TileTurretSAM;
import ballistix.registers.BallistixMenuTypes;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerSAMTurret extends GenericContainerBlockEntity<TileTurretSAM> {

    public ContainerSAMTurret(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(1), new SimpleContainerData(3));
    }

    public ContainerSAMTurret(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(BallistixMenuTypes.CONTAINER_SAMTURRET.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container container, Inventory inventory) {

        setPlayerInvOffset(10);

        addSlot(new SlotGeneric(container, nextIndex(), 80, 20).setIOColor(new Color(0, 240, 255, 255)));

    }
}
