package ballistix.common.block;

import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.stream.Stream;

public class BallistixVoxelShapes {

    public static void init() {

    }

    public static final VoxelShapeProvider RADAR = VoxelShapeProvider.createDirectional(
            //
            Direction.NORTH,
            //
            Stream.of(
                    //
                    Block.box(0, 0, 0, 16, 5, 16),
                    //
                    Block.box(4, 5, 3.5, 12, 10, 12.5),
                    //
                    Block.box(6, 10, 6, 10, 11, 10)
                    //
            ).reduce(Shapes::or).get()
            //
    );
    public static final VoxelShapeProvider MISSILE_SILO = VoxelShapeProvider.createOmni(Block.box(0, 0, 0, 16, 1, 16));

}
