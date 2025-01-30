package ballistix.registers;

import ballistix.References;
import ballistix.common.block.BlockExplosive;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.block.subtype.SubtypeBlast;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.block.BlockMachine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, References.ID);

	public static final BulkDeferredHolder<Block, BlockMachine, SubtypeBallistixMachine> BLOCKS_BALLISTIXMACHINE = new BulkDeferredHolder<>(SubtypeBallistixMachine.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockMachine(subtype)));
	public static final BulkDeferredHolder<Block, BlockExplosive, SubtypeBlast> BLOCKS_EXPLOSIVE = new BulkDeferredHolder<>(SubtypeBlast.values(), subtype -> BLOCKS.register(subtype.tag(), () -> new BlockExplosive(subtype)));

}
