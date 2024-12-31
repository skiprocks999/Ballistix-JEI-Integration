package ballistix.registers;

import ballistix.References;
import ballistix.common.block.BlockExplosive;
import ballistix.common.block.BlockMissileSilo;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.tile.TileRadar;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, References.ID);

	public static final DeferredHolder<Block, BlockMissileSilo> BLOCK_MISSILESILO = BLOCKS.register("missilesilo", BlockMissileSilo::new);
	public static final DeferredHolder<Block, GenericMachineBlock> BLOCK_RADAR = BLOCKS.register("radar", () -> new GenericMachineBlock(TileRadar::new, false));
	public static final BulkDeferredHolder<Block, BlockExplosive, SubtypeBlast> BLOCKS_EXPLOSIVE = new BulkDeferredHolder<>(SubtypeBlast.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockExplosive(subtype)));

}
