package ballistix.client.guidebook.chapters;

import ballistix.Ballistix;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.settings.Constants;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixItems;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterMissileDefense extends Chapter {

    private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar));

    public ChapterMissileDefense(Module module) {
        super(module);
    }

    @Override
    public AbstractGraphicWrapper<?> getLogo() {
        return LOGO;
    }

    @Override
    public MutableComponent getTitle() {
        return BallistixTextUtils.guidebook("chapter.missiledefense");
    }

    @Override
    public void addData() {

        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.l1")).setSeparateStart().setIndentions(1));

        // Search Radar
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.searchradar1", Constants.RADAR_RANGE)).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Ballistix.rl("textures/screen/guidebook/searchradar1.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 150, 150, 150, Ballistix.rl("textures/screen/guidebook/searchradar2.png")));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.searchradar2")).setSeparateStart());

        // Fire Control Radar
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.firecontrolradar1", BallistixItems.ITEM_RADARGUN.get().getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.MAX_DISTANCE_FROM_RADAR)).setSeparateStart().setIndentions(1));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.firecontrolradar2", Constants.FIRE_CONTROL_RADAR_RANGE)).setSeparateStart().setIndentions(1));

        // ESM Tower
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.esmtower1", Constants.ESM_TOWER_SEARCH_RADIUS)).setSeparateStart().setIndentions(1));

        // SAM Turret
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.samturret1", BallistixItems.ITEM_AAMISSILE.get().getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.SAM_TURRET_BASE_RANGE, Constants.SAM_TURRET_COOLDOWN)).setSeparateStart().setIndentions(1));

        // CIWS Turret
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.ciwsturret1", BallistixItems.ITEM_BULLET.get().getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.CIWS_TURRET_BASE_RANGE, Constants.MISSILE_HEALTH)).setSeparateStart().setIndentions(1));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.ciwsturret2")).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Ballistix.rl("textures/screen/guidebook/ciwsturret1.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 150, 150, 150, Ballistix.rl("textures/screen/guidebook/ciwsturret2.png")));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.ciwsturret3")).setSeparateStart());

        // Laser Turret
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.laserturret).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.laserturret)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.laserturret1", Constants.LASER_TURRET_BASE_RANGE)).setSeparateStart().setIndentions(1));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.laserturret2")).setSeparateStart().setIndentions(1));

        // Railgun Turret
        pageData.add(new TextWrapperObject(BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.railgunturret).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEMS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.railgunturret)));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.railgunturret1", ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.steel).getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.RAILGUN_TURRET_BASE_RANGE, Constants.RAILGUN_TURRET_COOLDOWN)).setSeparateStart().setIndentions(1));
        pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.missiledefense.railgunturret2")).setSeparateStart().setIndentions(1));


    }


}
