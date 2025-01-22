package ballistix.client.screen;

import ballistix.common.inventory.container.ContainerSAMTurret;
import electrodynamics.prefab.screen.GenericScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenSAMTurret extends GenericScreen<ContainerSAMTurret> {
    public ScreenSAMTurret(ContainerSAMTurret container, Inventory inv, Component title) {
        super(container, inv, title);
    }
}
