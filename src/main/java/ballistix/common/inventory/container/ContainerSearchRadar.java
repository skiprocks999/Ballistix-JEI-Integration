package ballistix.common.inventory.container;

import ballistix.common.packet.type.client.PacketSetSearchRadarTrackedClient;
import ballistix.common.tile.radar.TileSearchRadar;
import ballistix.registers.BallistixMenuTypes;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;

public class ContainerSearchRadar extends GenericContainerBlockEntity<TileSearchRadar> {

    public ContainerSearchRadar(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(0), new SimpleContainerData(3));
    }

    public ContainerSearchRadar(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(BallistixMenuTypes.CONTAINER_SEARCHRADAR.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container container, Inventory inventory) {

    }

    @Override
    public void addPlayerInventory(Inventory playerinv) {

    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if(!getLevel().isClientSide() && getPlayer() != null && getSafeHost() != null) {
            PacketSetSearchRadarTrackedClient packet = new PacketSetSearchRadarTrackedClient(new HashSet<>(getSafeHost().detections), getSafeHost().getBlockPos());
            PacketDistributor.sendToPlayer((ServerPlayer) getPlayer(), packet);
        }
    }
}
