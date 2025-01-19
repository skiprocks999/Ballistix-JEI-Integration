package ballistix.registers;

import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.tile.TileFireControlRadar;
import com.google.common.collect.Sets;

import ballistix.References;
import ballistix.common.tile.TileMissileSilo;
import ballistix.common.tile.TileRadar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixTiles {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, References.ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMissileSilo>> TILE_MISSILESILO = BLOCK_ENTITY_TYPES.register("missilesilo", () -> new BlockEntityType<>(TileMissileSilo::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRadar>> TILE_RADAR = BLOCK_ENTITY_TYPES.register("radar", () -> new BlockEntityType<>(TileRadar::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFireControlRadar>> TILE_FIRECONTROLRADAR = BLOCK_ENTITY_TYPES.register("firecontrolradar", () -> new BlockEntityType<>(TileFireControlRadar::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar)), null));

}
