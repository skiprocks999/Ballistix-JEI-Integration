package ballistix.common.blast;

import java.util.Iterator;

import ballistix.common.blast.thread.raycast.ThreadRaycastBlast;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import ballistix.compatibility.griefdefender.GriefDefenderHandler;
import electrodynamics.common.packet.types.client.PacketSpawnSmokeParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlastThermobaric extends Blast {

    public BlastThermobaric(Level world, BlockPos position) {
        super(world, position);
    }

    @Override
    public void doPreExplode() {
        if (!world.isClientSide) {
            thread = new ThreadRaycastBlast(world, position, (int) Constants.EXPLOSIVE_THERMOBARIC_SIZE, (float) Constants.EXPLOSIVE_THERMOBARIC_ENERGY, null);
            thread.start();
            world.playSound(null, position, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 25, 1);
        }

    }

    private ThreadRaycastBlast thread;
    private int pertick = -1;
    private Iterator<BlockPos> cachedIterator;

    @Override
    public boolean doExplode(int callCount) {
        if (world.isClientSide) {
            return false;
        }
        if (thread == null) {
            return true;
        }
        Explosion ex = new Explosion(world, null, null, null, position.getX(), position.getY(), position.getZ(), (float) Constants.EXPLOSIVE_THERMOBARIC_SIZE, false, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
        if (thread.isComplete) {
            synchronized (thread.resultsSync) {
                if (pertick == -1) {
                    hasStarted = true;
                    pertick = (int) (thread.resultsSync.size() / Constants.EXPLOSIVE_THERMOBARIC_DURATION + 1);
                    cachedIterator = thread.resultsSync.iterator();
                }
                int finished = pertick;
                while (cachedIterator.hasNext()) {
                    if (finished-- < 0) {
                        break;
                    }
                    BlockPos p = new BlockPos(cachedIterator.next());
                    Block block = world.getBlockState(p).getBlock();
                    switch (griefPreventionMethod) {
                        case NONE :
                            block.wasExploded(world, p, ex);
                            world.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
                            break;
                        case GRIEF_DEFENDER:
                            GriefDefenderHandler.destroyBlock(block, ex, p, world);
                            break;
                        case SABER_FACTIONS:


                            break;
                    }
                    if (world.random.nextFloat() < 1 / 10.0 && world instanceof ServerLevel serverlevel) {
                        serverlevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(p), false).forEach(pl -> PacketDistributor.sendToPlayer(pl, new PacketSpawnSmokeParticle(p)));
                    }
                }
                if (!cachedIterator.hasNext()) {
                    attackEntities((float) Constants.EXPLOSIVE_THERMOBARIC_SIZE * 2, ex);
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
        return SubtypeBlast.thermobaric;
    }
    // TODO: Finish block model

}
