package ballistix.client.screen;

import ballistix.Ballistix;
import ballistix.common.inventory.container.ContainerSAMTurret;
import ballistix.common.settings.Constants;
import ballistix.common.tile.turret.antimissile.TileTurretSAM;
import ballistix.common.tile.turret.antimissile.util.TileTurretAntimissile;
import ballistix.prefab.BallistixIconTypes;
import ballistix.prefab.utils.BallistixTextUtils;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGuiTab;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ScreenSAMTurret extends GenericScreen<ContainerSAMTurret> {
    public ScreenSAMTurret(ContainerSAMTurret container, Inventory inv, Component title) {
        super(container, inv, title);

        inventoryLabelY += 10;
        imageHeight += 10;

        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.SAM_TURRET_USAGEPERTICK * 20));

        addComponent(new ScreenComponentGuiTab(ScreenComponentGuiTab.GuiInfoTabTextures.REGULAR, BallistixIconTypes.TARGET, () -> {
            List<FormattedCharSequence> text = new ArrayList<>();
            TileTurretSAM turret = menu.getSafeHost();
            if(turret == null) {
                return text;

            }
            text.add(BallistixTextUtils.tooltip("turret.blockrange").withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            text.add(BallistixTextUtils.tooltip("turret.maxrange", Component.literal("" + turret.range).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            text.add(BallistixTextUtils.tooltip("turret.minrange", Component.literal("" + turret.minimumRange).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            return text;
        }, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2));

        new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE * 2 + 2, 75, 92, 8, 82);

        addComponent(new ScreenComponentSimpleLabel(10, 50, 10, Color.WHITE, () -> {
            TileTurretAntimissile turret = menu.getSafeHost();
            if(turret == null) {
                return Component.empty();
            }
            Component radar = turret.isNotLinked.get() ? BallistixTextUtils.gui("turret.radarnone").withStyle(ChatFormatting.RED) : Component.literal(turret.boundFireControl.get().toShortString()).withStyle(ChatFormatting.DARK_GRAY);

            return BallistixTextUtils.gui("turret.radar", radar).withStyle(ChatFormatting.BLACK);
        }));

        addComponent(new ScreenComponentSimpleLabel(10, 65, 10, Color.WHITE, () -> {
            TileTurretSAM turret = menu.getSafeHost();
            if(turret == null) {
                return Component.empty();
            }
            Component status;

            if(turret.hasNoPower.get()) {
                status = BallistixTextUtils.gui("turret.statusnopower").withStyle(ChatFormatting.RED);
            } else if (turret.isNotLinked.get()) {
                status = BallistixTextUtils.gui("turret.statusunlinked").withStyle(ChatFormatting.RED);
            } else if (!turret.hasTarget.get()) {
                status = BallistixTextUtils.gui("turret.statusnotarget").withStyle(ChatFormatting.GREEN);
            } else if (!turret.inRange.get()) {
                status = BallistixTextUtils.gui("turret.statusoutofrange").withStyle(ChatFormatting.YELLOW);
            } else if (turret.outOfAmmo.get()) {
                status = BallistixTextUtils.gui("turret.statusnoammo").withStyle(ChatFormatting.RED);
            } else if (turret.cooldown.get() > 0) {
                status = BallistixTextUtils.gui("turret.statuscooldown", turret.cooldown.get()).withStyle(ChatFormatting.RED);
            } else {
                status = BallistixTextUtils.gui("turret.statusgood", turret.cooldown.get()).withStyle(ChatFormatting.GREEN);
            }


            return BallistixTextUtils.gui("turret.status", status).withStyle(ChatFormatting.BLACK);
        }));
    }
}
