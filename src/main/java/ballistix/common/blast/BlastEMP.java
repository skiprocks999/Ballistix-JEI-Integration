package ballistix.common.blast;

import java.util.Iterator;

import ballistix.common.blast.thread.ThreadSimpleBlast;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class BlastEMP extends Blast implements IHasCustomRenderer {

	public BlastEMP(Level world, BlockPos position) {
		super(world, position);
	}

	@Override
	public void doPreExplode() {
		if (!world.isClientSide) {
			thread = new ThreadSimpleBlast(world, position, (int) Constants.EXPLOSIVE_EMP_RADIUS, Integer.MAX_VALUE, null, true);
			thread.start();
		}
	}

	private ThreadSimpleBlast thread;
	private int pertick = -1;

	@Override
	public boolean shouldRender() {
		return pertick > 0;
	}

	private Iterator<BlockPos> cachedIterator;

	@Override
	public boolean doExplode(int callCount) {
		if (world.isClientSide) {
			return false;
		}
		if (thread == null) {
			return true;
		}
		if (!thread.isComplete) {
			return false;
		}
		hasStarted = true;
		if (pertick == -1) {
			pertick = (int) (thread.results.size() / Constants.EXPLOSIVE_ANTIMATTER_DURATION + 1);
			cachedIterator = thread.results.iterator();
		}
		int finished = pertick;
		while (cachedIterator.hasNext()) {
			if (finished-- < 0) {
				break;
			}
			BlockPos p = new BlockPos(cachedIterator.next()).offset(position);
			BlockEntity entity = world.getBlockEntity(p);
			if (entity != null) {
				for (Direction dir : Direction.values()) {

					ICapabilityElectrodynamic electro = world.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, p, world.getBlockState(p), entity, dir);

					if(electro != null) {

						electro.setJoulesStored(0);

					} else {
						IEnergyStorage fe = world.getCapability(Capabilities.EnergyStorage.BLOCK, p, world.getBlockState(p), entity, dir);

						if(fe != null) {
							fe.extractEnergy(Integer.MAX_VALUE, false);
						}
					}
				}
			} // TODO: Implement player inventory energy clearing
		}
		if (!cachedIterator.hasNext()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isInstantaneous() {
		return false;
	}

	@Override
	public SubtypeBlast getBlastType() {
		return SubtypeBlast.emp;
	}

}
