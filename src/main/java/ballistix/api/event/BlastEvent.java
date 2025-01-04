package ballistix.api.event;

import ballistix.common.blast.Blast;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class BlastEvent extends Event implements ICancellableEvent {

	public Level world;
	public Blast iExplosion;

	public BlastEvent(Level world, Blast iExplosion) {
		this.world = world;
		this.iExplosion = iExplosion;
	}

	public static class ConstructBlastEvent extends BlastEvent {

		public ConstructBlastEvent(Level world, Blast explosion) {
			super(world, explosion);
		}
	}

	public static class PreBlastEvent extends BlastEvent {

		public PreBlastEvent(Level world, Blast explosion) {
			super(world, explosion);
		}
	}

	public static class DoExplosionEvent extends BlastEvent {

		public DoExplosionEvent(Level world, Blast explosion) {
			super(world, explosion);
		}
	}

	public static class PostBlastEvent extends BlastEvent {

		public PostBlastEvent(Level world, Blast explosion) {
			super(world, explosion);
		}
	}
}