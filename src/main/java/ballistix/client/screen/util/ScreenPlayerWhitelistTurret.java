package ballistix.client.screen.util;

import ballistix.common.tile.turret.GenericTileTurret;
import ballistix.prefab.screen.WrapperPlayerWhitelist;
import com.mojang.blaze3d.platform.InputConstants;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentVerticalSlider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class ScreenPlayerWhitelistTurret<T extends GenericContainerBlockEntity<? extends GenericTileTurret>> extends GenericScreen<T> {

    public ScreenComponentVerticalSlider whitelistSlider;
    public WrapperPlayerWhitelist whitelistWrapper;

    public ScreenPlayerWhitelistTurret(T container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    public abstract void updateVisibility(boolean show);

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        playerInvLabel.setVisible(false);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        whitelistWrapper.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (whitelistWrapper != null) {
            if (scrollY > 0) {
                // scroll up
                whitelistWrapper.handleMouseScroll(-1);
            } else if (scrollY < 0) {
                // scroll down
                whitelistWrapper.handleMouseScroll(1);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (whitelistSlider != null && whitelistSlider.isVisible()) {
            whitelistSlider.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (whitelistSlider != null && whitelistSlider.isVisible()) {
            whitelistSlider.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (whitelistSlider.isVisible()) {
            return whitelistSlider.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && whitelistWrapper.addEditBox.isActive()) {
            return false;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

}
