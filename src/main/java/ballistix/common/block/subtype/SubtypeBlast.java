package ballistix.common.block.subtype;

import ballistix.common.blast.*;
import electrodynamics.api.ISubtype;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;

public enum SubtypeBlast implements ISubtype {
	obsidian(BlastObsidian::new, 120),
	condensive(BlastCondensive::new, 30),
	attractive(BlastAttractive::new, 30),
	repulsive(BlastRepulsive::new, 30),
	incendiary(BlastIncendiary::new, 80),
	shrapnel(BlastShrapnel::new, 40),
	debilitation(BlastDebilitation::new, 80),
	chemical(BlastChemical::new, 100),
	emp(BlastEMP::new, 80),
	breaching(BlastBreaching::new, 5),
	thermobaric(BlastThermobaric::new, 100),
	contagious(BlastContagious::new, 100),
	fragmentation(BlastFragmentation::new, 100),
	landmine(BlastLandmine::new, 5, VoxelShapeProvider.createOmni(Shapes.create(0, 0, 0, 16.0 / 16.0, 3.0 / 16.0, 16.0 / 16.0))),
	nuclear(BlastNuclear::new, 200),
	antimatter(BlastAntimatter::new, 400),
	largeantimatter(BlastLargeAntimatter::new, 600),
	darkmatter(BlastDarkmatter::new, 400);

	public final Blast.BlastFactory<?> factory;
	public final int fuse;
	public final VoxelShapeProvider shape;

	SubtypeBlast(Blast.BlastFactory<?> factory, int fuse, VoxelShapeProvider shape) {
		this.factory = factory;
		this.fuse = fuse;
		this.shape = shape;
	}

	SubtypeBlast(Blast.BlastFactory<?> factory, int fuse) {
		this(factory, fuse, VoxelShapeProvider.DEFAULT);
	}

	@Override
	public String forgeTag() {
		return tag();
	}

	@Override
	public boolean isItem() {
		return true;
	}

	@Override
	public String tag() {
		return name();
	}

	public Blast createBlast(Level world, BlockPos pos) {
		return factory.create(world, pos);
	}
}
