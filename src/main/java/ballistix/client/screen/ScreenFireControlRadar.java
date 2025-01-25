package ballistix.client.screen;

import ballistix.common.block.subtype.SubtypeMissile;
import ballistix.common.inventory.container.ContainerFireControlRadar;
import ballistix.common.item.ItemMissile;
import ballistix.common.settings.Constants;
import ballistix.common.tile.radar.TileFireControlRadar;
import ballistix.prefab.BallistixIconTypes;
import ballistix.prefab.screen.WrapperFrequencyManager;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixItems;
import com.mojang.blaze3d.platform.InputConstants;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentCustomRender;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.ScreenComponentVerticalSlider;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGuiTab;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ScreenFireControlRadar extends GenericScreen<ContainerFireControlRadar> {

    public final ScreenComponentVerticalSlider slider;
    public final WrapperFrequencyManager frequencyWrapper;
    public final ScreenComponentCustomRender trackingLabel;
    public final ScreenComponentCustomRender trackingRender;

    public ScreenFireControlRadar(ContainerFireControlRadar container, Inventory inv, Component title) {
        super(container, inv, title);

        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.FIRE_CONTROL_RADAR_USAGE));
        addComponent(new ScreenComponentGuiTab(ScreenComponentGuiTab.GuiInfoTabTextures.REGULAR, ScreenComponentSlot.IconType.SONAR_PROFILE, () -> {
            List<FormattedCharSequence> info = new ArrayList<>();

            TileFireControlRadar radar = menu.getSafeHost();

            if (radar == null) {
                return info;
            }

            info.add(BallistixTextUtils.tooltip("turret.blockrange").withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            info.add(BallistixTextUtils.tooltip("turret.maxrange", ChatFormatter.formatDecimals(Constants.FIRE_CONTROL_RADAR_RANGE, 1).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());


            return info;

        }, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2));

        frequencyWrapper = new WrapperFrequencyManager(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE * 2 + 2, 0, 0);

        addComponent(slider = new ScreenComponentVerticalSlider(11, 80, 75).setClickConsumer(frequencyWrapper.getSliderClickedConsumer()).setDragConsumer(frequencyWrapper.getSliderDraggedConsumer()));

        slider.setVisible(false);

        addComponent(trackingLabel = new ScreenComponentCustomRender(10, 30, graphics -> {
            TileFireControlRadar tile = menu.getSafeHost();
            if (tile == null) {
                return;
            }
            Component radar = tile.trackingPos.get().equals(TileFireControlRadar.OUT_OF_REACH) ? BallistixTextUtils.gui("turret.radarnone").withStyle(ChatFormatting.GREEN) : Component.literal(tile.trackingPos.get().toString()).withStyle(ChatFormatting.DARK_GRAY);

            int x = (int) (getGuiWidth() + 10);
            int y = (int) (getGuiHeight() + 30);

            Component label = BallistixTextUtils.gui("radar.tracking").withStyle(ChatFormatting.BLACK);

            int width = getFontRenderer().width(label);
            int height = getFontRenderer().lineHeight;

            graphics.drawString(getFontRenderer(), label, x, y, Color.WHITE.color(), false);

            x += width;

            float scale = 1.0F;

            width = font.width(radar);

            if (width > 100) {
                scale = 100.0F / (float) width;
            }

            float remHeight = (height - (float) height * scale) / 2.0F;

            graphics.pose().pushPose();

            graphics.pose().translate(x, y + remHeight, 0);

            graphics.pose().scale(scale, scale, scale);

            graphics.drawString(getFontRenderer(), radar, 0, 0, Color.WHITE.color(), false);

            graphics.pose().popPose();


        }));

        addComponent(trackingRender = new ScreenComponentCustomRender(10, 50, graphics -> {
            TileFireControlRadar tile = menu.getSafeHost();
            if (tile == null) {
                return;
            }

            int missileType = tile.missileType.get();

            if (missileType < 0) {
                return;
            }

            int x = (int) (getGuiWidth() + 65);
            int y = (int) (getGuiHeight() + 80);

            SubtypeMissile missileEnum = SubtypeMissile.values()[missileType];

            Item missile = BallistixItems.ITEMS_MISSILE.getValue(missileEnum);

            RenderingUtils.renderItemScaled(graphics, missile, x, y, 5.0F);

        }));

        addComponent(new ScreenComponentButton<>(ScreenComponentGuiTab.GuiInfoTabTextures.REGULAR, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE * 3 + 2)
                //
                .setOnPress(button -> {

                    TileFireControlRadar tile = menu.getSafeHost();

                    if(tile == null) {
                        return;
                    }

                    tile.usingRedstone.set(!tile.usingRedstone.get());


                })
                //
                .setIcon(BallistixIconTypes.REDSTONE)
                //
                .onTooltip((graphics, button, x, y) -> {

                    TileFireControlRadar tile = menu.getSafeHost();

                    if(tile == null) {
                        return;
                    }

                    List<FormattedCharSequence> info = new ArrayList<>();

                    info.add(BallistixTextUtils.tooltip("radar.redstone").withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());

                    String key = tile.usingRedstone.get() ? "radar.redstone.enabled" : "radar.redstone.disabled";

                    info.add(BallistixTextUtils.tooltip(key).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC).getVisualOrderText());

                    graphics.renderTooltip(getFontRenderer(), info, x, y);

                }));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        frequencyWrapper.tick();
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        playerInvLabel.setVisible(false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (frequencyWrapper != null) {
            if (scrollY > 0) {
                // scroll up
                frequencyWrapper.handleMouseScroll(-1);
            } else if (scrollY < 0) {
                // scroll down
                frequencyWrapper.handleMouseScroll(1);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (slider != null && slider.isVisible()) {
            slider.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (slider != null && slider.isVisible()) {
            slider.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && frequencyWrapper.addEditBox.isActive()) {
            return false;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (slider.isVisible()) {
            return slider.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

}
