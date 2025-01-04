package ballistix.compatibility.nuclearscience;

import ballistix.common.settings.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nuclearscience.api.radiation.RadiationSystem;
import nuclearscience.api.radiation.SimpleRadiationSource;
import nuclearscience.common.block.BlockIrradiated;
import nuclearscience.registers.NuclearScienceBlocks;

public class RadiationHandler {

    public static void addNuclearExplosionRadiation(Level world, BlockPos location) {
        RadiationSystem.addRadiationSource(world, new SimpleRadiationSource(150000.0, 2, (int) (Constants.EXPLOSIVE_NUCLEAR_SIZE), false, 0, location, false));
    }

    public static void addNuclearExplosiveIrradidatedBlock(BlockPos p, Level world) {
        BlockState state = world.getBlockState(p);;

        if(BlockIrradiated.isValidPlacement(state) && world.random.nextFloat() < 0.7) {
            world.setBlock(p, BlockIrradiated.getIrradiatedBlockstate(state), 3);
        } else if (state.is(BlockTags.LEAVES)) {
            world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
        } else if (state.isAir()) {
            world.setBlock(p, NuclearScienceBlocks.BLOCK_RADIOACTIVEAIR.get().defaultBlockState(), 3);
        }
    }

}
