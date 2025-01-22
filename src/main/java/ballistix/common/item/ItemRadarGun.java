package ballistix.common.item;

import java.util.List;

import ballistix.common.tile.TileMissileSilo;
import ballistix.common.tile.antimissile.turret.GenericTileAMTurret;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixCreativeTabs;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.math.MathUtils;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemRadarGun extends ItemElectric {

	public static final double USAGE = 150.0;

	public ItemRadarGun() {
		super((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), BallistixCreativeTabs.MAIN, item -> ElectrodynamicsItems.ITEM_BATTERY.get());
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		if (context.getLevel().isClientSide || !stack.has(ElectrodynamicsDataComponentTypes.BLOCK_POS)) {
			return super.onItemUseFirst(stack, context);
		}
		BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());

		if(tile instanceof TileMissileSilo silo) {

			silo.target.set(stack.get(ElectrodynamicsDataComponentTypes.BLOCK_POS));

		} else if (tile instanceof TileMultiSubnode subnode && subnode.getLevel().getBlockEntity(subnode.parentPos.get()) instanceof TileMissileSilo silo) {

			silo.target.set(stack.get(ElectrodynamicsDataComponentTypes.BLOCK_POS));

		} else if (tile instanceof GenericTileAMTurret turret) {
			turret.bindFireControlRadar(stack.get(ElectrodynamicsDataComponentTypes.BLOCK_POS));
		}

		return super.onItemUseFirst(stack, context);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return super.useOn(context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

		if (worldIn.isClientSide) {
			return super.use(worldIn, playerIn, handIn);
		}

		Location trace = MathUtils.getRaytracedBlock(playerIn);

		if (trace == null) {
			return super.use(worldIn, playerIn, handIn);
		}

		ItemStack radarGun = playerIn.getItemInHand(handIn);

		if (getJoulesStored(radarGun) < USAGE) {
			return super.use(worldIn, playerIn, handIn);
		}

		//prevents using the radar gun on missile silo from overriding the stored coords

		if(trace.getTile(playerIn.level()) instanceof TileMissileSilo || trace.getTile(playerIn.level()) instanceof TileMultiSubnode subnode && subnode.getLevel().getBlockEntity(subnode.parentPos.get()) instanceof TileMissileSilo || trace.getTile(worldIn) instanceof GenericTileAMTurret) {
			return super.use(worldIn, playerIn, handIn);
		}

		radarGun.set(ElectrodynamicsDataComponentTypes.BLOCK_POS, trace.toBlockPos());

		extractPower(radarGun, USAGE, false);

		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

		if (!worldIn.isClientSide || !isSelected) {
			return;
		}

		Location trace = MathUtils.getRaytracedBlock(entityIn);

		if (trace == null) {
			return;
		}

		if (entityIn instanceof Player player) {
			player.displayClientMessage(BallistixTextUtils.chatMessage("radargun.text", trace.toBlockPos().toShortString()), true);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		if(stack.has(ElectrodynamicsDataComponentTypes.BLOCK_POS)) {
			tooltip.add(BallistixTextUtils.tooltip("radargun.pos", stack.get(ElectrodynamicsDataComponentTypes.BLOCK_POS).toShortString()).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(BallistixTextUtils.tooltip("radargun.notag").withStyle(ChatFormatting.GRAY));
		}
	}

}
