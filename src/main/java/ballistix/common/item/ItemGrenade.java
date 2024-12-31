package ballistix.common.item;

import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.entity.EntityGrenade;
import ballistix.registers.BallistixCreativeTabs;
import electrodynamics.api.ISubtype;
import electrodynamics.common.item.ItemElectrodynamics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemGrenade extends ItemElectrodynamics {

	private SubtypeGrenade grenade;

	public ItemGrenade(SubtypeGrenade grenade) {
		super(new Item.Properties().stacksTo(16), BallistixCreativeTabs.MAIN);
		this.grenade = grenade;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 60;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	}

	@Override
	public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entityLiving, int timeLeft) {
		if (world.isClientSide) {
			return;
		}
		world.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

		float throwEnergy = (float) (getUseDuration(itemStack, entityLiving) - timeLeft) / (float) getUseDuration(itemStack, entityLiving) + 0.7f;

		EntityGrenade grenade = new EntityGrenade(world);
		grenade.moveTo(entityLiving.getX(), entityLiving.getY() + entityLiving.getEyeHeight() * 0.8, entityLiving.getZ(), entityLiving.getYRot(), entityLiving.getXRot());
		grenade.setExplosiveType(this.grenade);
		grenade.shootFromRotation(entityLiving, entityLiving.getXRot() - 20, entityLiving.getYRot(), 0.0F, throwEnergy, 1.0F);
		world.addFreshEntity(grenade);
		if (entityLiving instanceof Player pl && !pl.isCreative() || !(entityLiving instanceof Player)) {
			itemStack.shrink(1);
		}
	}

	public enum SubtypeGrenade implements ISubtype {
		attractive(SubtypeBlast.attractive),
		chemical(SubtypeBlast.chemical),
		condensive(SubtypeBlast.condensive),
		debilitation(SubtypeBlast.debilitation),
		incendiary(SubtypeBlast.incendiary),
		repulsive(SubtypeBlast.repulsive),
		shrapnel(SubtypeBlast.shrapnel);

		public final SubtypeBlast explosiveType;

		SubtypeGrenade(SubtypeBlast explosive) {
			explosiveType = explosive;
		}

		@Override
		public String forgeTag() {
			return "grenades/" + name();
		}

		@Override
		public boolean isItem() {
			return true;
		}

		@Override
		public String tag() {
			return "grenade" + name();
		}

	}

}
