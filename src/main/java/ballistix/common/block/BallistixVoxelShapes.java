package ballistix.common.block;

import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.world.level.block.Block;

public class BallistixVoxelShapes {

	public static void init() {

	}

	public static final VoxelShapeProvider MISSILE_SILO = VoxelShapeProvider.createOmni(Block.box(0, 0, 0, 16, 1, 16));

}
