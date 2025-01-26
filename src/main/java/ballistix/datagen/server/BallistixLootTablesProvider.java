package ballistix.datagen.server;

import java.util.List;

import ballistix.References;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.registers.BallistixTiles;
import ballistix.registers.BallistixBlocks;
import electrodynamics.datagen.server.ElectrodynamicsLootTablesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

public class BallistixLootTablesProvider extends ElectrodynamicsLootTablesProvider {

	public BallistixLootTablesProvider(HolderLookup.Provider provider) {
		super(References.ID, provider);
	}

	@Override
	protected void generate() {

		for (SubtypeBlast blast : SubtypeBlast.values()) {
			addSimpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(blast));
		}

		addMachineTable(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo), BallistixTiles.TILE_MISSILESILO, true, false, false, false, false);
		addSimpleBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar));
		addSimpleBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar));
		addSimpleBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret));
		addSimpleBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower));

	}

	@Override
	public List<Block> getExcludedBlocks() {
		return List.of();
	}

}
