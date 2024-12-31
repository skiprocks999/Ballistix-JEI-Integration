package ballistix.registers;

import ballistix.References;
import ballistix.common.entity.EntityBlast;
import ballistix.common.entity.EntityExplosive;
import ballistix.common.entity.EntityGrenade;
import ballistix.common.entity.EntityMinecart;
import ballistix.common.entity.EntityMissile;
import ballistix.common.entity.EntityShrapnel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BallistixEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, References.ID);

	public static final DeferredHolder<EntityType<?>, EntityType<EntityExplosive>> ENTITY_EXPLOSIVE = ENTITIES.register("explosive", () -> EntityType.Builder.<EntityExplosive>of(EntityExplosive::new, MobCategory.MISC).fireImmune().sized(1, 1).clientTrackingRange(10).build(References.ID + ".explosive"));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityGrenade>> ENTITY_GRENADE = ENTITIES.register("grenade", () -> EntityType.Builder.<EntityGrenade>of(EntityGrenade::new, MobCategory.MISC).fireImmune().sized(0.25f, 0.55f).build(References.ID + ".grenade"));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityMinecart>> ENTITY_MINECART = ENTITIES.register("minecart", () -> EntityType.Builder.<EntityMinecart>of(EntityMinecart::new, MobCategory.MISC).fireImmune().clientTrackingRange(8).sized(0.98F, 0.7F).build(References.ID + ".minecart"));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityBlast>> ENTITY_BLAST = ENTITIES.register("blast", () -> EntityType.Builder.<EntityBlast>of(EntityBlast::new, MobCategory.MISC).fireImmune().build(References.ID + ".blast"));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityShrapnel>> ENTITY_SHRAPNEL = ENTITIES.register("shrapnel", () -> EntityType.Builder.<EntityShrapnel>of(EntityShrapnel::new, MobCategory.MISC).fireImmune().sized(0.5f, 0.5f).build(References.ID + ".shrapnel"));
	public static final DeferredHolder<EntityType<?>, EntityType<EntityMissile>> ENTITY_MISSILE = ENTITIES.register("missile", () -> EntityType.Builder.<EntityMissile>of(EntityMissile::new, MobCategory.MISC).fireImmune().sized(0, 0).build(References.ID + ".missile"));
}
