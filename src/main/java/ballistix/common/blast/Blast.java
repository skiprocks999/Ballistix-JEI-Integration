package ballistix.common.blast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import ballistix.common.settings.Constants;
import com.google.common.collect.Maps;

import ballistix.api.event.BlastEvent;
import ballistix.api.event.BlastEvent.ConstructBlastEvent;
import ballistix.api.event.BlastEvent.PostBlastEvent;
import ballistix.api.event.BlastEvent.PreBlastEvent;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.entity.EntityBlast;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.EventHooks;

public abstract class Blast {

	private static final UUID FAKE_PLAYER_ID = UUID.fromString("111aa11a-11a1-111a-aaaa-a1a11a111123");
	private static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(FAKE_PLAYER_ID, "Ballistix Explosive Player");

	public BlockPos position;
	public Level world;
	public boolean hasStarted;

	protected Blast(Level world, BlockPos position) {
		this.world = world;
		this.position = position;
	}

	public boolean isInstantaneous() {
		return true;
	}

	public abstract SubtypeBlast getBlastType();

	public void doPreExplode() {
	}

	public abstract boolean doExplode(int callCount);

	public void doPostExplode() {
	}

	@Deprecated(since = "Should not be called externally!", forRemoval = false)
	public final void preExplode() {
		PreBlastEvent evt = new PreBlastEvent(world, this);
		NeoForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled()) {
			doPreExplode();
		}
	}

	@Deprecated(since = "Should not be called externally!", forRemoval = false)
	public final boolean explode(int callcount) {
		BlastEvent evt = new BlastEvent(world, this);
		NeoForge.EVENT_BUS.post(evt);
		if (!evt.isCanceled()) {
			return doExplode(callcount);
		}
		return true;
	}

	@Deprecated(since = "Should not be called externally!", forRemoval = false)
	public final void postExplode() {
		PostBlastEvent evt = new PostBlastEvent(world, this);
		NeoForge.EVENT_BUS.post(evt);

		if (!evt.isCanceled()) {
			doPostExplode();
		}
	}

	public EntityBlast performExplosion() {
		ConstructBlastEvent evt = new ConstructBlastEvent(world, this);
		NeoForge.EVENT_BUS.post(evt);
		Explosion explosion = new Explosion(world, null, null, null, position.getX(), position.getY(), position.getZ(), 3, true, BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE);
		if (!EventHooks.onExplosionStart(world, explosion) && !evt.isCanceled()) {
			if (isInstantaneous()) {
				doPreExplode();
				doExplode(0);
				doPostExplode();
			} else if (!world.isClientSide) {
				EntityBlast entity = new EntityBlast(world);
				entity.setPos(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5);
				entity.setBlastType(getBlastType());
				world.addFreshEntity(entity);
				return entity;
			}
		}
		return null;
	}

	protected void attackEntities(float size, Explosion explosion) {
		this.attackEntities(size, true, explosion);
	}

	protected void attackEntities(float size, boolean useRaytrace, Explosion explosion) {
		Map<Player, Vec3> playerKnockbackMap = Maps.newHashMap();
		float doubleSize = size * 2.0F;
		int x0 = Mth.floor(position.getX() - (double) doubleSize - 1.0D);
		int x1 = Mth.floor(position.getX() + (double) doubleSize + 1.0D);
		int y0 = Mth.floor(position.getY() - (double) doubleSize - 1.0D);
		int y1 = Mth.floor(position.getY() + (double) doubleSize + 1.0D);
		int z0 = Mth.floor(position.getZ() - (double) doubleSize - 1.0D);
		int z1 = Mth.floor(position.getZ() + (double) doubleSize + 1.0D);

		List<Entity> entities = world.getEntities(null, new AABB(x0, y0, z0, x1, y1, z1));

		Vec3 posVector = new Vec3(position.getX(), position.getY(), position.getZ());

		for (Entity entity : entities) {
			if(entity.ignoreExplosion(explosion)) {
				continue;
			}
			double normalizedDiameter = Mth.sqrt((float) entity.distanceToSqr(posVector)) / doubleSize;
			if (normalizedDiameter > 1.0D) {
				continue;
			}
			double deltaX = entity.getX() - position.getX();
			double deltaY = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - position.getY();
			double deltaZ = entity.getZ() - position.getZ();
			double deltaDistance = Mth.sqrt((float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));

			if(deltaDistance == 0.0D) {
				continue;
			}

			deltaX = deltaX / deltaDistance;
			deltaY = deltaY / deltaDistance;
			deltaZ = deltaZ / deltaDistance;

			double seenAmount = useRaytrace ? Explosion.getSeenPercent(posVector, entity) : 1;

			double damageAmount = (1.0D - normalizedDiameter) * seenAmount;

			entity.hurt(entity.damageSources().explosion(null, null), (int) ((damageAmount * damageAmount + damageAmount) / 2.0D * 7.0D * doubleSize + 1.0D));

			double actualDamange = damageAmount;

			if (entity instanceof LivingEntity le) {
				double damage = damageAmount;
				int i = EnchantmentHelper.getEnchantmentLevel(world.registryAccess().holderOrThrow(Enchantments.BLAST_PROTECTION), le);
				if (i > 0) {
					damage *= Mth.clamp(1.0D - (double)i * 0.15D, 0.0D, 1.0D);
				}

				actualDamange = damage;
			}

			entity.setDeltaMovement(entity.getDeltaMovement().add(deltaX * actualDamange, deltaY * actualDamange, deltaZ * actualDamange));
			if (entity instanceof Player playerentity) {
				if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.getAbilities().flying)) {
					playerKnockbackMap.put(playerentity, new Vec3(deltaX * damageAmount, deltaY * damageAmount, deltaZ * damageAmount));
				}
			}
		}
		for (Entry<Player, Vec3> entry : playerKnockbackMap.entrySet()) {
			if (entry.getKey() instanceof ServerPlayer serverplayerentity) {
				serverplayerentity.connection.send(new ClientboundExplodePacket(position.getX(), position.getY(), position.getZ(), size, new ArrayList<>(), entry.getValue(), BlockInteraction.DESTROY, ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE));
			}
		}
	}

	//public FakePlayer getFakePlayer(ServerLevel level) {
	//	return Conastants.SHOULD_EXPLOSIONS_BYPASS_CLAIMS ? null : FakePlayerFactory.get(level, FAKE_PLAYER_PROFILE);
	//}

	public static interface BlastFactory<T extends Blast> {
		T create(Level world, BlockPos pos);
	}
}
