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
import ballistix.registers.*;
import electrodynamics.client.guidebook.ScreenGuidebook;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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
		ItemProperties.register(BallistixItems.ITEM_TRACKER.get(), ANGLE_PREDICATE, (stack, level, entity, seed) -> {
			//
			Entity sourceEntity = entity != null ? entity : stack.getEntityRepresentation();
			if (sourceEntity == null || !stack.has(BallistixDataComponentTypes.TRACKER_TARGET)) {
				return 0F;
			}

			ItemTracker.Target target = stack.get(BallistixDataComponentTypes.TRACKER_TARGET);

			double angleOfSource = 0.0D;
			if (entity instanceof Player player && player.isLocalPlayer()) {
				angleOfSource = entity.getYRot();
			} else if (sourceEntity instanceof ItemFrame itemFrameEntity) {
				Direction direction = itemFrameEntity.getDirection();
				int j = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
				angleOfSource = Mth.wrapDegrees(180 + direction.get2DDataValue() * 90L + itemFrameEntity.getRotation() * 45L + j);
			} else if (sourceEntity instanceof ItemEntity item) {
				angleOfSource = 180.0F - item.getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
			} else if (entity != null) {
				angleOfSource = entity.yBodyRot;
			}

			double rawAngleToTarget = Math.atan2(target.z() - sourceEntity.getZ(), target.x() - sourceEntity.getX()) / ((float) Math.PI * 2F);
			double adjustedAngleToTarget = 0.5D - (Mth.positiveModulo(angleOfSource / 360.0D, 1.0D) - 0.25D - rawAngleToTarget);

			return Mth.positiveModulo((float) adjustedAngleToTarget, 1.0F);
			//
		});

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
