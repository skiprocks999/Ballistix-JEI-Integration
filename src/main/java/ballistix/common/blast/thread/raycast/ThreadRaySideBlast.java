package ballistix.common.blast.thread.raycast;

import electrodynamics.Electrodynamics;
import electrodynamics.prefab.block.HashDistanceBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class ThreadRaySideBlast extends Thread {

	public ThreadRaycastBlast mainBlast;

	public Direction direction;
	private final RandomSource random = RandomSource.createThreadSafe();
	private final IResistanceCallback callback;
	private final int explosionRadius;
	private final BlockPos position;
	private final Level world;
	private final Entity explosionSource;
	private final float explosionEnergy;
	private final Set<BlockPos> resultSync;

	private static final float DEFAULT_POWER_DEC = 0.03F * 0.75F * 5F;

	public ThreadRaySideBlast(ThreadRaycastBlast threadRaycastBlast, Direction dir) {
		mainBlast = threadRaycastBlast;
		direction = dir;
		this.callback = threadRaycastBlast.callBack;
		this.explosionSource = threadRaycastBlast.explosionSource;
		this.position = mainBlast.position;
		this.explosionRadius = mainBlast.explosionRadius;
		this.world = mainBlast.level;
		this.explosionEnergy = mainBlast.explosionEnergy;
		this.resultSync = threadRaycastBlast.resultsSync;
		setName("Raycast Blast Side Thread");
	}

	@Override
	@SuppressWarnings("java:S2184")
	public void run() {

		Electrodynamics.LOGGER.info("dir " + direction);
		long time = System.currentTimeMillis();

		int iMin = -explosionRadius, iMax = explosionRadius, jMax = explosionRadius, jMin = -explosionRadius;
		Vec3i orientation = direction.getNormal();
		for (int i = iMin; i < iMax; i++) {
			for (int j = jMin; j < jMax; j++) {

				int x = 0, y = 0, z = 0;

				if (orientation.getX() != 0) {
					x = orientation.getX() * explosionRadius;
					y += i;
					z += j;
				} else if (orientation.getY() != 0) {
					x += i;
					y = orientation.getY() * explosionRadius;
					z += j;
				} else if (orientation.getZ() != 0) {
					x += i;
					y += j;
					z = orientation.getZ() * explosionRadius;
				}

				Vec3 delta = new Vec3(x, y, z).normalize();

				float power = explosionEnergy - (explosionEnergy * random.nextFloat() / 2);

				Vec3 currentVector = new Vec3(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5);

				BlockPos currentBlockPos = new BlockPos((int) Math.floor(currentVector.x()), (int) Math.floor(currentVector.y()), (int) Math.floor(currentVector.z()));

				int air = 0;

				while(power > 0.0F) {

					if(air > 10) {
						break;
					}

					BlockPos next = new BlockPos((int) Math.floor(currentVector.x()), (int) Math.floor(currentVector.y()), (int) Math.floor(currentVector.z()));
					currentVector = new Vec3(currentVector.x + delta.x, currentVector.y + delta.y, currentVector.z + delta.z);

					if (!next.equals(currentBlockPos)) {
						currentBlockPos = next;
						BlockState block = world.getBlockState(currentBlockPos);
						if (!block.isAir()) {
							if (block.getDestroySpeed(world, currentBlockPos) >= 0) {
								power -= Math.max(1, callback.getResistance(world, position, currentBlockPos, explosionSource, block));
								if (power > 0f) {
									int idistancesq = (int) (Math.pow(currentBlockPos.getX() - position.getX(), 2) + Math.pow(currentBlockPos.getY() - position.getY(), 2) + Math.pow(currentBlockPos.getZ() - position.getZ(), 2));
									resultSync.add(new HashDistanceBlockPos(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ(), idistancesq));
								}
							} else {
								power = 0;
							}
						} else {
							air++;
						}
					}

					power -= DEFAULT_POWER_DEC;
				}

				/*
				for (float d = 0.3F; power > 0f; power -= (d * 0.75F * 5)) {

					Electrodynamics.LOGGER.info("power " + power);

					BlockPos next = new BlockPos((int) Math.floor(currentVector.x()), (int) Math.floor(currentVector.y()), (int) Math.floor(currentVector.z()));

					// move to front to allow for use of continue

					currentVector = new Vec3(currentVector.x + delta.x, currentVector.y + delta.y, currentVector.z + delta.z);

					if (next.equals(currentBlockPos)) {
						continue;
					}

					currentBlockPos = next;

					BlockState block = world.getBlockState(currentBlockPos);

					if (block.isAir()) {
						continue;
					}

					if (block.getDestroySpeed(world, currentBlockPos) >= 0) {
						power -= Math.max(1, mainBlast.callBack.getResistance(world, position, currentBlockPos, mainBlast.explosionSource, block));
						if (power > 0f) {
							int idistancesq = (int) (Math.pow(currentBlockPos.getX() - position.getX(), 2) + Math.pow(currentBlockPos.getY() - position.getY(), 2) + Math.pow(currentBlockPos.getZ() - position.getZ(), 2));
							synchronized (mainBlast.resultsSync) {
								mainBlast.resultsSync.add(new HashDistanceBlockPos(currentBlockPos.getX(), currentBlockPos.getY(), currentBlockPos.getZ(), idistancesq));
							}
						}
					} else {
						power = 0;
					}
				}

				 */
			}
		}

		Electrodynamics.LOGGER.info("dir " + direction + ", time" + (System.currentTimeMillis() - time) / 1000);
		mainBlast.underBlasts.remove(this);
	}
}