package ballistix.registers;

import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.tile.TileESMTower;
import ballistix.common.tile.radar.TileFireControlRadar;
import ballistix.common.tile.turret.antimissile.TileTurretCIWS;
import ballistix.common.tile.turret.antimissile.TileTurretSAM;
import com.google.common.collect.Sets;

import ballistix.References;
import ballistix.common.tile.TileMissileSilo;
import ballistix.common.tile.radar.TileSearchRadar;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixTiles {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, References.ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMissileSilo>> TILE_MISSILESILO = BLOCK_ENTITY_TYPES.register("missilesilo", () -> new BlockEntityType<>(TileMissileSilo::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSearchRadar>> TILE_RADAR = BLOCK_ENTITY_TYPES.register("radar", () -> new BlockEntityType<>(TileSearchRadar::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFireControlRadar>> TILE_FIRECONTROLRADAR = BLOCK_ENTITY_TYPES.register("firecontrolradar", () -> new BlockEntityType<>(TileFireControlRadar::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileESMTower>> TILE_ESMTOWER = BLOCK_ENTITY_TYPES.register("esmtower", () -> new BlockEntityType<>(TileESMTower::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileTurretSAM>> TILE_SAMTURRET = BLOCK_ENTITY_TYPES.register("samturret", () -> new BlockEntityType<>(TileTurretSAM::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileTurretCIWS>> TILE_CIWSTURRET = BLOCK_ENTITY_TYPES.register("ciwsturret", () -> new BlockEntityType<>(TileTurretCIWS::new, Sets.newHashSet(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret)), null));

}
