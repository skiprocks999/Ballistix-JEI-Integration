package ballistix.common.tile;

import ballistix.common.tile.radar.TileFireControlRadar;
import ballistix.common.tile.radar.TileSearchRadar;
import ballistix.registers.BallistixTiles;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class TileESMTower extends GenericTile {

    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileSearchRadar>> SEARCH_RADARS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileFireControlRadar>> FIRE_CONTROL_RADARS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileESMTower>> ESM_TOWERS = new ConcurrentHashMap<>();


    public TileESMTower(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_ESMTOWER.get(), worldPos, blockState);
    }
}
