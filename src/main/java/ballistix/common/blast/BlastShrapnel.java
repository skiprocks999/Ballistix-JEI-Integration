package ballistix.common.blast;

import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.entity.EntityShrapnel;
import ballistix.common.settings.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class BlastShrapnel extends Blast {

	public BlastShrapnel(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	public void doPreExplode() {
		if(!world.isClientSide) {
			world.playSound(null, position, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 25, 1);
		}
	}

	@Override
	public boolean doExplode(int callCount) {
		hasStarted = true;
		for (int i = 0; i < Constants.EXPLOSIVE_SHRAPNEL_SHRAPNEL_COUNT; i++) {
			EntityShrapnel shrapnel = new EntityShrapnel(world);
			float yaw = world.random.nextFloat() * 360;
			float pitch = world.random.nextFloat() * 90 - 75;
			shrapnel.moveTo(position.getX(), position.getY(), position.getZ(), yaw, pitch);
			shrapnel.shootFromRotation(null, pitch, yaw, 0.0F, 2f, 0.0F);
			world.addFreshEntity(shrapnel);
		}
		return true;
	}

	@Override
	public SubtypeBlast getBlastType() {
		return SubtypeBlast.shrapnel;
	}

}
