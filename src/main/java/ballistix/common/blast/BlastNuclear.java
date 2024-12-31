package ballistix.common.blast;

import java.util.Iterator;

import ballistix.References;
import ballistix.common.blast.thread.ThreadSimpleBlast;
import ballistix.common.blast.thread.raycast.ThreadRaycastBlast;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import ballistix.compatibility.nuclearscience.RadiationHandler;
import ballistix.registers.BallistixSounds;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.common.packet.types.client.PacketSpawnSmokeParticle;
import electrodynamics.prefab.utilities.object.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlastNuclear extends Blast implements IHasCustomRenderer {

	public BlastNuclear(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	public void doPreExplode() {
		if (!world.isClientSide) {
			threadRay = new ThreadRaycastBlast(world, position, (int) Constants.EXPLOSIVE_NUCLEAR_SIZE, (float) Constants.EXPLOSIVE_NUCLEAR_ENERGY, null);
			threadSimple = new ThreadSimpleBlast(world, position, (int) (Constants.EXPLOSIVE_NUCLEAR_SIZE * 2), Integer.MAX_VALUE, null, true);
			threadSimple.strictnessAtEdges = 1.7;
			threadRay.start();
			threadSimple.start();
		} else {
			SoundAPI.playSound(BallistixSounds.SOUND_NUCLEAREXPLOSION.get(), SoundSource.BLOCKS, 1, 1, position);
		}
	}

	private Iterator<BlockPos> cachedIteratorRay;
	private Iterator<BlockPos> cachedIterator;

	private ThreadRaycastBlast threadRay;
	private ThreadSimpleBlast threadSimple;
	private int pertick = -1;
	private int perticksimple = -1;
	private int particleHeight = 0;

	@Override
	public boolean shouldRender() {
		return pertick > 0;
	}

	@Override
	public boolean doExplode(int callCount) {
		if (!world.isClientSide) {
			if (threadRay == null) {
				return true;
			}
			Explosion ex = new Explosion(world, null, null, null, position.getX(), position.getY(), position.getZ(), (float) Constants.EXPLOSIVE_NUCLEAR_SIZE, false, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
			boolean rayDone = false;
			if (threadRay.isComplete && !rayDone) {
				hasStarted = true;
				synchronized (threadRay.resultsSync) {
					if (pertick == -1) {
						pertick = (int) (threadRay.resultsSync.size() / Constants.EXPLOSIVE_NUCLEAR_DURATION + 1);
						cachedIteratorRay = threadRay.resultsSync.iterator();
					}
					int finished = pertick;
					while (cachedIteratorRay.hasNext()) {
						if (finished-- < 0) {
							break;
						}
						BlockPos p = new BlockPos(cachedIteratorRay.next());

						BlockState state = Blocks.AIR.defaultBlockState();
						double dis = new Location(p.getX(), 0, p.getZ()).distance(new Location(position.getX(), 0, position.getZ()));
						if (world.random.nextFloat() < 1 / 5.0 && dis < 15) {
							BlockPos offset = p.relative(Direction.DOWN);
							if (!threadRay.results.contains(offset) && world.random.nextFloat() < (15.0f - dis) / 15.0f) {
								state = Blocks.FIRE.defaultBlockState();
							}
						}
						world.getBlockState(p).getBlock().wasExploded(world, p, ex);
						world.setBlock(p, state, 2);
						if (world.random.nextFloat() < 1 / 20.0 && world instanceof ServerLevel serverlevel) {
							serverlevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(p), false).forEach(pl -> PacketDistributor.sendToPlayer(pl, new PacketSpawnSmokeParticle(p)));
						}
					}
					if (particleHeight < 23) {
						int radius = 2;
						if (particleHeight > 18) {
							radius = 25 + 20 - particleHeight;
						}
						if (particleHeight > 20) {
							radius = 25 - 20 + particleHeight;
						}
						for (int i = -radius; i <= radius; i++) {
							for (int k = -radius; k <= radius; k++) {
								if (i * i + k * k < radius * radius && world.random.nextFloat() < (particleHeight > 18 ? 0.1 : 0.3)) {
									BlockPos p = position.offset(i, particleHeight, k);
									((ServerLevel) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(p), false).forEach(pl -> PacketDistributor.sendToPlayer(pl, new PacketSpawnSmokeParticle(p)));
								}
							}
						}
						particleHeight++;
					}
					if (!cachedIteratorRay.hasNext()) {
						rayDone = true;
					}
					if (ModList.get().isLoaded(References.NUCLEAR_SCIENCE_ID)) {
						RadiationHandler.addNuclearExplosionRadiation(world, position);
					}
				}
			}
			if (threadSimple.isComplete && rayDone) {
				if (!ModList.get().isLoaded(References.NUCLEAR_SCIENCE_ID)) {
					attackEntities((float) Constants.EXPLOSIVE_NUCLEAR_SIZE * 2, ex);
					return true;
				}
				if (perticksimple == -1) {
					cachedIterator = threadSimple.results.iterator();
					perticksimple = (int) (threadSimple.results.size() * 1.5 / Constants.EXPLOSIVE_NUCLEAR_DURATION + 1);
				}
				int finished = perticksimple;
				while (cachedIterator.hasNext()) {
					if (finished-- < 0) {
						break;
					}
					RadiationHandler.addNuclearExplosiveIrradidatedBlock(new BlockPos(cachedIterator.next()).offset(position), world);
				}
				if (!cachedIterator.hasNext()) {
					attackEntities((float) Constants.EXPLOSIVE_NUCLEAR_SIZE * 2, ex);
					return true;
				}
			}
		}
		return false;

	}

	@Override
	public boolean isInstantaneous() {
		return false;
	}

	@Override
	public SubtypeBlast getBlastType() {
		return SubtypeBlast.nuclear;
	}
	// TODO: Finish block model
}
