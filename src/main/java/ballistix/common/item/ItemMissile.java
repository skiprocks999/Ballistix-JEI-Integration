package ballistix.common.item;

import ballistix.common.block.subtype.SubtypeMissile;
import ballistix.common.settings.Constants;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixCreativeTabs;
import electrodynamics.common.item.ItemElectrodynamics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemMissile extends ItemElectrodynamics {

	public final SubtypeMissile missile;

	public ItemMissile(SubtypeMissile missile) {
		super(new Item.Properties(), BallistixCreativeTabs.MAIN);
		this.missile = missile;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

		int range = switch(missile) {
			case closerange -> Constants.CLOSERANGE_MISSILE_RANGE;
			case mediumrange -> Constants.MEDIUMRANGE_MISSILE_RANGE;
			case longrange -> Constants.LONGRANGE_MISSILE_RANGE;
		};

		Component rangeText = Component.literal(range + "");

		if(range < 0) {
			rangeText = BallistixTextUtils.tooltip("missile.unlimited");
		}

		tooltipComponents.add(BallistixTextUtils.tooltip("missile.range", rangeText).withStyle(ChatFormatting.GRAY));
	}
}
