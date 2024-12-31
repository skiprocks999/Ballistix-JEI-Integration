package ballistix.client;

import ballistix.References;
import ballistix.client.guidebook.ModuleBallistix;
import ballistix.client.render.entity.RenderBlast;
import ballistix.client.render.entity.RenderExplosive;
import ballistix.client.render.entity.RenderGrenade;
import ballistix.client.render.entity.RenderMinecart;
import ballistix.client.render.entity.RenderMissile;
import ballistix.client.render.entity.RenderShrapnel;
import ballistix.client.render.tile.RenderMissileSilo;
import ballistix.client.render.tile.RenderRadar;
import ballistix.client.screen.ScreenMissileSilo;
import ballistix.common.item.ItemTracker;
import ballistix.registers.BallistixTiles;
import ballistix.registers.BallistixEntities;
import ballistix.registers.BallistixItems;
import ballistix.registers.BallistixMenuTypes;
import electrodynamics.client.guidebook.ScreenGuidebook;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD, value = { Dist.CLIENT })
public class ClientRegister {

	public static final ResourceLocation ANGLE_PREDICATE = ResourceLocation.parse("angle");

	public static final ResourceLocation TEXTURE_SHRAPNEL = ResourceLocation.parse(References.ID + ":textures/model/shrapnel.png");
	public static final ResourceLocation TEXTURE_MISSILECLOSERANGE = ResourceLocation.parse(References.ID + ":textures/model/missilecloserange.png");
	public static final ResourceLocation TEXTURE_MISSILEMEDIUMRANGE = ResourceLocation.parse(References.ID + ":textures/model/missilemediumrange.png");
	public static final ResourceLocation TEXTURE_MISSILELONGRANGE = ResourceLocation.parse(References.ID + ":textures/model/missilelongrange.png");
	
	public static final ModelResourceLocation MODEL_RADARDISH = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":block/dish"));
	public static final ModelResourceLocation MODEL_MISSILECLOSERANGE = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/missilecloserange"));
	public static final ModelResourceLocation MODEL_MISSILEMEDIUMRANGE = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/missilemediumrange"));
	public static final ModelResourceLocation MODEL_MISSILELONGRANGE = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/missilelongrange"));
	public static final ModelResourceLocation MODEL_DARKMATTERSPHERE = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/darkmattersphere"));
	public static final ModelResourceLocation MODEL_DARKMATTERDISK = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/darkmatterdisk"));
	public static final ModelResourceLocation MODEL_FIREBALL = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/explosionsphere"));
	public static final ModelResourceLocation MODEL_EMP = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/emp"));
	public static final ModelResourceLocation MODEL_BLACKHOLECUBE = ModelResourceLocation.standalone(ResourceLocation.parse(References.ID + ":entity/blackhole"));

	public static void setup() {
		ItemProperties.register(BallistixItems.ITEM_TRACKER.get(), ANGLE_PREDICATE, ItemTracker::getAngle);

		ScreenGuidebook.addGuidebookModule(new ModuleBallistix());
	}

	@SubscribeEvent
	public static void registerMenus(RegisterMenuScreensEvent event) {
		event.register(BallistixMenuTypes.CONTAINER_MISSILESILO.get(), ScreenMissileSilo::new);
	}

	@SubscribeEvent
	public static void onModelEvent(ModelEvent.RegisterAdditional event) {
		event.register(MODEL_RADARDISH);
		event.register(MODEL_MISSILECLOSERANGE);
		event.register(MODEL_MISSILEMEDIUMRANGE);
		event.register(MODEL_MISSILELONGRANGE);
		event.register(MODEL_DARKMATTERSPHERE);
		event.register(MODEL_DARKMATTERDISK);
		event.register(MODEL_FIREBALL);
		event.register(MODEL_EMP);
		event.register(MODEL_BLACKHOLECUBE);
	}

	@SubscribeEvent
	public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(BallistixEntities.ENTITY_EXPLOSIVE.get(), RenderExplosive::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_GRENADE.get(), RenderGrenade::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_BLAST.get(), RenderBlast::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_SHRAPNEL.get(), RenderShrapnel::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MISSILE.get(), RenderMissile::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MINECART.get(), RenderMinecart::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_MISSILESILO.get(), RenderMissileSilo::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_RADAR.get(), RenderRadar::new);

	}

}
