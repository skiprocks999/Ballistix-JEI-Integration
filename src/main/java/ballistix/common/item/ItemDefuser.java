package ballistix.common.item;

import ballistix.References;
import ballistix.api.entity.IDefusable;
import ballistix.registers.BallistixCreativeTabs;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME)
public class ItemDefuser extends ItemElectric {

	public static final double USAGE = 150;

	public ItemDefuser() {
		super((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), BallistixCreativeTabs.MAIN, item -> ElectrodynamicsItems.ITEM_BATTERY.get());
	}

	@SubscribeEvent
	public static void onInteractWithEntity(PlayerInteractEvent.EntityInteractSpecific event) {

		Level world = event.getLevel();

		if (world.isClientSide) {
			return;
		}

		Player playerIn = event.getEntity();
		Entity entity = event.getTarget();

		ItemStack stack = playerIn.getItemInHand(event.getHand());

		boolean validItem = stack.getItem() instanceof ItemDefuser defuser && defuser.getJoulesStored(stack) >= USAGE;

		if (!validItem) {
			return;
		}

		ItemDefuser defuser = (ItemDefuser) stack.getItem();

		if (entity instanceof IDefusable defuse) {

			defuser.extractPower(stack, USAGE, false);
			defuse.defuse();

		} else if (entity instanceof PrimedTnt tnt) {

			entity.remove(RemovalReason.DISCARDED);

			ItemEntity item = new ItemEntity(world, tnt.getBlockX() + 0.5, tnt.getBlockY() + 0.5, tnt.getBlockZ() + 0.5, new ItemStack(Items.TNT));
			defuser.extractPower(stack, 150, false);
			world.addFreshEntity(item);

		}
	}
}
