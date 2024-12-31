package ballistix.common.block;

import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.entity.EntityExplosive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockExplosive extends Block {
	public final SubtypeBlast explosive;

	public BlockExplosive(SubtypeBlast explosive) {
		super(Blocks.TNT.properties().instabreak().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor((a, b, c) -> false));
		this.explosive = explosive;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return explosive.shape.apply(state, level, pos, context);
	}

	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
		explode(world, pos, explosive);
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!oldState.is(state.getBlock()) && worldIn.hasNeighborSignal(pos)) {
			onCaughtFire(state, worldIn, pos, null, null);
			worldIn.removeBlock(pos, false);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity ent) {
		super.entityInside(state, level, pos, ent);
		if (explosive == SubtypeBlast.landmine) {
			explode(level, pos, explosive);
			level.removeBlock(pos, false);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (worldIn.hasNeighborSignal(pos)) {
			onCaughtFire(state, worldIn, pos, null, null);
			worldIn.removeBlock(pos, false);
		}

	}

	@Override
	public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isClientSide) {
			explode(worldIn, pos, explosive);
		}
	}

	private static void explode(Level worldIn, BlockPos pos, SubtypeBlast explosive) {
		if (!worldIn.isClientSide) {
			EntityExplosive explosiveEntity = new EntityExplosive(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			explosiveEntity.setBlastType(explosive);
			worldIn.addFreshEntity(explosiveEntity);
			worldIn.playSound((Player) null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		}
		onCaughtFire(state, level, pos, hitResult.getDirection(), player);
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
		if (!player.isCreative() && !level.isClientSide()) {
			if (item == Items.FLINT_AND_STEEL) {
				itemstack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
			} else {
				itemstack.shrink(1);
			}
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!worldIn.isClientSide) {
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire()) {
				BlockPos blockpos = hit.getBlockPos();
				onCaughtFire(state, worldIn, blockpos, null, entity instanceof LivingEntity l ? l : null);
				worldIn.removeBlock(blockpos, false);
			}
		}
	}

	@Override
	public boolean dropFromExplosion(Explosion explosionIn) {
		return false;
	}
}
