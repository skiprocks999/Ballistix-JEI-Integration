package ballistix.client.guidebook.chapters;

import ballistix.common.item.ItemDefuser;
import ballistix.common.item.ItemLaserDesignator;
import ballistix.common.item.ItemRadarGun;
import ballistix.common.item.ItemScanner;
import ballistix.common.item.ItemTracker;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixItems;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterItems extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_ROCKETLAUNCHER.get());

	public ChapterItems(Module module) {
		super(module);
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return BallistixTextUtils.guidebook("chapter.items");
	}

	@Override
	public void addData() {

		// Rocket Launcher
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_ROCKETLAUNCHER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setSeparateStart());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_ROCKETLAUNCHER.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.rocketlauncher1")).setSeparateStart().setIndentions(1));

		// Radar Gun
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_RADARGUN.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_RADARGUN.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.radargun1", ChatFormatter.getChatDisplayShort(ItemRadarGun.USAGE, DisplayUnit.JOULES))).setSeparateStart().setIndentions(1));

		// Laser Designator
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_LASERDESIGNATOR.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_LASERDESIGNATOR.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.laserdesignator1")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.laserdesignator2", ChatFormatter.getChatDisplayShort(ItemLaserDesignator.USAGE, DisplayUnit.JOULES))).setSeparateStart().setIndentions(1));

		// Defuser
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_DEFUSER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_DEFUSER.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.defuser1", ChatFormatter.getChatDisplayShort(ItemDefuser.USAGE, DisplayUnit.JOULES))).setSeparateStart().setIndentions(1));

		// Tracker
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_TRACKER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_TRACKER.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.tracker1", ChatFormatter.getChatDisplayShort(ItemTracker.USAGE, DisplayUnit.JOULES))).setSeparateStart().setIndentions(1));

		// Scanner
		pageData.add(new TextWrapperObject(BallistixItems.ITEM_SCANNER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 10, 32, 32, 32, 2.0F, BallistixItems.ITEM_SCANNER.get()));
		pageData.add(new TextWrapperObject(BallistixTextUtils.guidebook("chapter.items.scanner1", ChatFormatter.getChatDisplayShort(ItemScanner.USAGE, DisplayUnit.JOULES))).setSeparateStart().setIndentions(1));

	}

}
