package ballistix.datagen.client;

import java.util.Locale;

import ballistix.References;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.registers.BallistixBlocks;
import electrodynamics.datagen.client.ElectrodynamicsBlockStateProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BallistixBlockStateProvider extends ElectrodynamicsBlockStateProvider {

	public BallistixBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, exFileHelper, References.ID);
	}

	@Override
	protected void registerStatesAndModels() {

		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.attractive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.breaching), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.chemical), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.condensive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.contagious), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.debilitation), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.emp), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.fragmentation), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.incendiary), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.nuclear), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.obsidian), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.repulsive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.shrapnel), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.thermobaric), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);

		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.antimatter), existingBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.antimatter)), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.darkmatter), existingBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.darkmatter)), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.landmine), existingBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.landmine)), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.largeantimatter), existingBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.largeantimatter)), true);

		horrRotatedBlock(BallistixBlocks.BLOCK_MISSILESILO, existingBlock(BallistixBlocks.BLOCK_MISSILESILO), 90, 0, false);
		horrRotatedBlock(BallistixBlocks.BLOCK_RADAR, existingBlock(BallistixBlocks.BLOCK_RADAR), 90, 0, false);

	}

	private void simpleExplosive(Block block, ExplosiveParent parent, boolean registerItem) {
		BlockModelBuilder builder = models().withExistingParent(name(block), blockLoc(parent.toString())).texture("3", blockLoc(name(block) + "base")).texture("particle", "#3");
		getVariantBuilder(block).partialState().setModels(new ConfiguredModel(builder));
		if (registerItem) {
			simpleBlockItem(block, builder);
		}
	}

	public enum ExplosiveParent {

		EXPLOSIVE_MODEL_ONE;

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.ROOT).replaceAll("_", "");
		}

	}

}
