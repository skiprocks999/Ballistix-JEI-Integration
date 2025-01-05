package ballistix.common.blast;

import java.util.Iterator;

import ballistix.common.blast.thread.ThreadSimpleBlast;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import ballistix.registers.BallistixSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlastLargeAntimatter extends Blast implements IHasCustomRenderer {

    public BlastLargeAntimatter(Level world, BlockPos position) {
        super(world, position);
    }

    @Override
    public void doPreExplode() {
        if (!world.isClientSide) {
            thread = new ThreadSimpleBlast(world, position, (int) Constants.EXPLOSIVE_LARGEANTIMATTER_RADIUS, Integer.MAX_VALUE, null, true, getBlastType().ordinal());
            thread.start();
            world.playSound(null, position, BallistixSounds.SOUND_ANTIMATTEREXPLOSION.get(), SoundSource.BLOCKS, 25, 1);
        }
    }

    private ThreadSimpleBlast thread;
    private int pertick = -1;

    @Override
    public boolean shouldRender() {
        return pertick > 0;
    }

    private Iterator<BlockPos> iterator;

    @Override
    public boolean doExplode(int callCount) {
        if (world.isClientSide) {
            return false;
        }
        if (thread == null) {
            return true;
        }
        Explosion ex = new Explosion(world, null, null, null, position.getX(), position.getY(), position.getZ(), (float) Constants.EXPLOSIVE_LARGEANTIMATTER_RADIUS, false, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
        if (!thread.isComplete) {
            return false;
        }
        hasStarted = true;
        if (pertick == -1) {
            pertick = (int) (thread.results.size() * 1.5 / Constants.EXPLOSIVE_LARGEANTIMATTER_DURATION + 1);
            iterator = thread.results.iterator();
        }
        int finished = pertick;
        while (iterator.hasNext()) {
            if (finished-- < 0) {
                break;
            }
            BlockPos p = new BlockPos(iterator.next()).offset(position);
            BlockState state = world.getBlockState(p);
            Block block = state.getBlock();

            if (!state.isAir() && state.getDestroySpeed(world, p) >= 0) {
                block.wasExploded(world, p, ex);
                world.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
            }
        }
        if (!iterator.hasNext()) {
            position = position.above().above();
            attackEntities((float) Constants.EXPLOSIVE_LARGEANTIMATTER_RADIUS * 2, ex);
            return true;
        }
        return false;
    }

    @Override
    public boolean isInstantaneous() {
        return false;
    }

    @Override
    public SubtypeBlast getBlastType() {
        return SubtypeBlast.largeantimatter;
    }

}
