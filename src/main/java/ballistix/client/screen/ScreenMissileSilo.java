package ballistix.client.screen;

import ballistix.common.inventory.container.ContainerMissileSilo;
import ballistix.common.settings.Constants;
import ballistix.common.tile.TileMissileSilo;
import ballistix.prefab.utils.BallistixTextUtils;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentFillArea;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMissileSilo extends GenericScreen<ContainerMissileSilo> {

	private boolean needsUpdate = true;

	private final ScreenComponentEditBox xCoordField;
	private final ScreenComponentEditBox yCoordField;
	private final ScreenComponentEditBox zCoordField;
	private final ScreenComponentEditBox frequencyField;


	public ScreenMissileSilo(ContainerMissileSilo container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);

		imageHeight += 20;
		inventoryLabelY += 20;

		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.MISSILESILO_USAGE * 20));

		addEditBox(xCoordField = new ScreenComponentEditBox(10, 20, 48, 15, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(10).setResponder(this::setX).setFilter(ScreenComponentEditBox.INTEGER));
		addEditBox(yCoordField = new ScreenComponentEditBox(10, 38, 48, 15, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(10).setResponder(this::setY).setFilter(ScreenComponentEditBox.INTEGER));
		addEditBox(zCoordField = new ScreenComponentEditBox(10, 56, 48, 15, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(10).setResponder(this::setZ).setFilter(ScreenComponentEditBox.INTEGER));
		addEditBox(frequencyField = new ScreenComponentEditBox(10, 74, 48, 15, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(10).setResponder(this::setFrequency).setFilter(ScreenComponentEditBox.INTEGER));



		addComponent(new ScreenComponentSimpleLabel(110, 24, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.missile")));
		addComponent(new ScreenComponentSimpleLabel(110, 45, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.explosive")));
		addComponent(new ScreenComponentSimpleLabel(60, 22, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.x")));
		addComponent(new ScreenComponentSimpleLabel(60, 40, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.y")));
		addComponent(new ScreenComponentSimpleLabel(60, 58, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.z")));
		addComponent(new ScreenComponentSimpleLabel(60, 76, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.freq")));
		addComponent(new ScreenComponentSimpleLabel(110, 74, 10, Color.TEXT_GRAY, BallistixTextUtils.gui("missilesilo.sync")));

		new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75, 102, 8, 92).hideAdditional(show -> {

		});
	}

	@Override
	protected void initializeComponents() {
		addComponent(new ScreenComponentFillArea(88, 18, 80, 71, new Color(120,120, 120, 255)));
		super.initializeComponents();
	}

	private void setSiloTargetX(String coord) {

		if (coord.isEmpty()) {
			return;
		}

		TileMissileSilo silo = menu.getSafeHost();

		if (silo == null) {
			return;
		}

		int x = silo.target.get().getX();

		try {
			x = Integer.parseInt(coord);
		} catch (Exception e) {
			// Filler
		}

		updateSiloCoords(x, silo.target.get().getY(), silo.target.get().getZ(), silo);

	}

	private void setSiloTargetY(String coord) {

		if (coord.isEmpty()) {
			return;
		}

		TileMissileSilo silo = menu.getSafeHost();

		if (silo == null) {
			return;
		}

		int y = silo.target.get().getY();

		try {
			y = Integer.parseInt(coord);
		} catch (Exception e) {
			// Filler
		}

		updateSiloCoords(silo.target.get().getX(), y, silo.target.get().getZ(), silo);

	}

	private void setSiloTargetZ(String coord) {

		if (coord.isEmpty()) {
			return;
		}

		TileMissileSilo silo = menu.getSafeHost();

		if (silo == null) {
			return;
		}

		int z = silo.target.get().getZ();

		try {
			z = Integer.parseInt(coord);
		} catch (Exception e) {
			// Filler
		}

		updateSiloCoords(silo.target.get().getX(), silo.target.get().getY(), z, silo);

	}

	private void updateSiloCoords(int x, int y, int z, TileMissileSilo silo) {

		silo.target.set(new BlockPos(x, y, z));

	}

	private void setSiloFrequency(String val) {

		if (val.isEmpty()) {
			return;
		}

		TileMissileSilo silo = menu.getSafeHost();

		if (silo == null) {
			return;
		}

		int frequency = 0;

		try {
			frequency = Integer.parseInt(val);
		} catch (Exception e) {
			// Filler
		}

		silo.frequency.set(frequency);

	}

	private void setFrequency(String val) {
		frequencyField.setFocus(true);
		xCoordField.setFocus(false);
		yCoordField.setFocus(false);
		zCoordField.setFocus(false);
		setSiloFrequency(val);
	}

	private void setX(String val) {
		xCoordField.setFocus(true);
		yCoordField.setFocus(false);
		zCoordField.setFocus(false);
		frequencyField.setFocus(false);
		setSiloTargetX(val);
	}

	private void setY(String val) {
		yCoordField.setFocus(true);
		xCoordField.setFocus(false);
		zCoordField.setFocus(false);
		frequencyField.setFocus(false);
		setSiloTargetY(val);
	}

	private void setZ(String val) {
		zCoordField.setFocus(true);
		yCoordField.setFocus(false);
		xCoordField.setFocus(false);
		frequencyField.setFocus(false);
		setSiloTargetZ(val);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TileMissileSilo silo = menu.getSafeHost();
			if (silo != null) {
				xCoordField.setValue("" + silo.target.get().getX());
				yCoordField.setValue("" + silo.target.get().getY());
				zCoordField.setValue("" + silo.target.get().getZ());
				frequencyField.setValue("" + silo.frequency.get());
			}
		}
	}

}