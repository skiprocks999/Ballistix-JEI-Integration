package ballistix.common.blast;

import java.util.List;

import org.joml.Vector3f;

import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import ballistix.registers.BallistixDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class BlastContagious extends Blast {

	public BlastContagious(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	public boolean isInstantaneous() {
		return false;
	}

	@Override
	public boolean doExplode(int callCount) {
		hasStarted = true;
		int radius = (int) Constants.EXPLOSIVE_CONTAGIOUS_SIZE;
		if (world.isClientSide && callCount % 3 == 0) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (x * x + y * y + z * z < radius * radius && world.random.nextDouble() < 1 / 20.0) {
							world.addParticle(new DustParticleOptions(new Vector3f(0.5f, 0.4f, 0), 5), position.getX() + x + 0.5 + world.random.nextDouble() - 1.0, position.getY() + y + 0.5 + world.random.nextDouble() - 1.0, position.getZ() + z + 0.5 + world.random.nextDouble() - 1.0, 0.0D, 0.0D, 0.0D);
						}
					}
				}
			}
		}
		if (!world.isClientSide) {

			float x = position.getX();
			float y = position.getY();
			float z = position.getZ();

			int x0 = Mth.floor(x - (double) radius - 1.0D);
			int x1 = Mth.floor(x + (double) radius + 1.0D);
			int y0 = Mth.floor(y - (double) radius - 1.0D);
			int y1 = Mth.floor(y + (double) radius + 1.0D);
			int z0 = Mth.floor(z - (double) radius - 1.0D);
			int z1 = Mth.floor(z + (double) radius + 1.0D);

			List<Entity> entities = world.getEntities(null, new AABB(x0, y0, z0, x1, y1, z1));

			for (Entity entity : entities) {
				if (entity instanceof LivingEntity living) {
					living.addEffect(new MobEffectInstance(MobEffects.POISON, 360, 2));
					living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 360, 2));
					living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 360, 3));
					if (callCount % 10 == 0) {
						living.hurt(living.damageSources().source(BallistixDamageTypes.CHEMICAL_GAS), 4);
					}
				}
			}
		}
		return callCount > Constants.EXPLOSIVE_CONTAGIOUS_DURATION;
	}

	@Override
	public SubtypeBlast getBlastType() {
		return SubtypeBlast.contagious;
	}

}
