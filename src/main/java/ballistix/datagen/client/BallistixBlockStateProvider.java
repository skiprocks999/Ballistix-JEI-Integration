package ballistix.datagen.client;

import java.util.Locale;

import ballistix.References;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
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

		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.attractive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.breaching), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.chemical), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.condensive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.contagious), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.debilitation), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.emp), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.fragmentation), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.incendiary), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.nuclear), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.obsidian), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.repulsive), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.shrapnel), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);
		//simpleExplosive(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.thermobaric), ExplosiveParent.EXPLOSIVE_MODEL_ONE, true);

		//Tier 0
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.obsidian), existingBlock(blockLoc("explosiveobsidian")), true);
		//Tier 1
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.chemical), existingBlock(blockLoc("explosivechemical")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.attractive), existingBlock(blockLoc("explosiveattractive")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.repulsive), existingBlock(blockLoc("explosiverepulsive")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.incendiary), existingBlock(blockLoc("explosiveincendiary")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.shrapnel), existingBlock(blockLoc("explosiveshrapnel")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.condensive), existingBlock(blockLoc("explosivecondensive")), true);
		//Tier 2
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.thermobaric), existingBlock(blockLoc("explosivethermobaric")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.breaching), existingBlock(blockLoc("explosivebreaching")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.debilitation), existingBlock(blockLoc("explosivedebilitation")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.contagious), existingBlock(blockLoc("explosivecontagious")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.fragmentation), existingBlock(blockLoc("explosivefragmentation")), true);
		//Tier 3
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.emp), existingBlock(blockLoc("explosiveemp")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.nuclear), existingBlock(blockLoc("explosivenuclear")), true);
		//Tier 4
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.antimatter), existingBlock(blockLoc("explosiveantimatter")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.darkmatter), existingBlock(blockLoc("explosivedarkmatter")), true);
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.largeantimatter), existingBlock(blockLoc("explosivelargeantimatter")), true);
		//Other
		simpleBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.landmine), existingBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.landmine)), true);



		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo)), 90, 0, false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar)), 90, 0, false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar)), 90, 0, false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret)),false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower)),false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret)),false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.laserturret), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.laserturret)),false);
		horrRotatedBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.railgunturret), existingBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.railgunturret)),false);

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
