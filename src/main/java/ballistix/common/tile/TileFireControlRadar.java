package ballistix.common.tile;

import ballistix.registers.BallistixTiles;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileFireControlRadar extends GenericTile {
    public TileFireControlRadar(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_FIRECONTROLRADAR.get(), worldPos, blockState);
    }
}
