package ballistix.client;

import ballistix.Ballistix;
import ballistix.References;
import ballistix.client.guidebook.ModuleBallistix;
import ballistix.client.render.entity.*;
import ballistix.client.render.tile.*;
import ballistix.client.screen.*;
import ballistix.common.item.ItemTracker;
import ballistix.registers.*;
import electrodynamics.Electrodynamics;
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

	public static final ResourceLocation ANGLE_PREDICATE = Electrodynamics.vanillarl("angle");

	public static final ResourceLocation TEXTURE_SHRAPNEL = Ballistix.rl("textures/model/shrapnel.png");
	public static final ResourceLocation TEXTURE_MISSILECLOSERANGE = Ballistix.rl("textures/model/missilecloserange.png");
	public static final ResourceLocation TEXTURE_MISSILEMEDIUMRANGE = Ballistix.rl("textures/model/missilemediumrange.png");
	public static final ResourceLocation TEXTURE_MISSILELONGRANGE = Ballistix.rl("textures/model/missilelongrange.png");
	
	public static final ModelResourceLocation MODEL_RADARDISH = ModelResourceLocation.standalone(Ballistix.rl("block/radardish"));
	public static final ModelResourceLocation MODEL_FIRECONTROLRADARDISH = ModelResourceLocation.standalone(Ballistix.rl("block/firecontrolradardish"));
	public static final ModelResourceLocation MODEL_MISSILECLOSERANGE = ModelResourceLocation.standalone(Ballistix.rl("entity/missilecloserange"));
	public static final ModelResourceLocation MODEL_MISSILEMEDIUMRANGE = ModelResourceLocation.standalone(Ballistix.rl("entity/missilemediumrange"));
	public static final ModelResourceLocation MODEL_MISSILELONGRANGE = ModelResourceLocation.standalone(Ballistix.rl("entity/missilelongrange"));
	public static final ModelResourceLocation MODEL_DARKMATTERSPHERE = ModelResourceLocation.standalone(Ballistix.rl("entity/darkmattersphere"));
	public static final ModelResourceLocation MODEL_DARKMATTERDISK = ModelResourceLocation.standalone(Ballistix.rl("entity/darkmatterdisk"));
	public static final ModelResourceLocation MODEL_FIREBALL = ModelResourceLocation.standalone(Ballistix.rl("entity/explosionsphere"));
	public static final ModelResourceLocation MODEL_EMP = ModelResourceLocation.standalone(Ballistix.rl("entity/emp"));
	public static final ModelResourceLocation MODEL_BLACKHOLECUBE = ModelResourceLocation.standalone(Ballistix.rl("entity/blackhole"));

	public static final ModelResourceLocation MODEL_AAMISSILE = ModelResourceLocation.standalone(Ballistix.rl("entity/aamissile"));
	public static final ModelResourceLocation MODEL_SAMTURRET_BALLJOINT = ModelResourceLocation.standalone(Ballistix.rl("block/samturretballjoint"));
	public static final ModelResourceLocation MODEL_SAMTURRET_RAIL = ModelResourceLocation.standalone(Ballistix.rl("block/samturretrail"));
	public static final ModelResourceLocation MODEL_ESMTOWER = ModelResourceLocation.standalone(Ballistix.rl("block/esmtower"));
	public static final ModelResourceLocation MODEL_CIWSTURRET_BALLJOINT = ModelResourceLocation.standalone(Ballistix.rl("block/ciwsturretballjoint"));
	public static final ModelResourceLocation MODEL_CIWSTURRET_HEAD = ModelResourceLocation.standalone(Ballistix.rl("block/ciwsturrethead"));
	public static final ModelResourceLocation MODEL_CIWSTURRET_BARREL = ModelResourceLocation.standalone(Ballistix.rl("block/ciwsturretbarrel"));
	public static final ModelResourceLocation MODEL_LASERTURRET_BALLJOINT = ModelResourceLocation.standalone(Ballistix.rl("block/laserturretballjoint"));
	public static final ModelResourceLocation MODEL_LASERTURRET_HEAD = ModelResourceLocation.standalone(Ballistix.rl("block/laserturrethead"));
	public static final ModelResourceLocation MODEL_RAILGUNTURRET_BALLJOINT = ModelResourceLocation.standalone(Ballistix.rl("block/railgunturretballjoint"));
	public static final ModelResourceLocation MODEL_RAILGUNTURRET_HEAD = ModelResourceLocation.standalone(Ballistix.rl("block/railgunturretgun"));


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
		event.register(BallistixMenuTypes.CONTAINER_SAMTURRET.get(), ScreenSAMTurret::new);
		event.register(BallistixMenuTypes.CONTAINER_FIRECONTROLRADAR.get(), ScreenFireControlRadar::new);
		event.register(BallistixMenuTypes.CONTAINER_SEARCHRADAR.get(), ScreenSearchRadar::new);
		event.register(BallistixMenuTypes.CONTAINER_ESMTOWER.get(), ScreenESMTower::new);
		event.register(BallistixMenuTypes.CONTAINER_CIWSTURRET.get(), ScreenCIWSTurret::new);
		event.register(BallistixMenuTypes.CONTAINER_LASERTURRET.get(), ScreenLaserTurret::new);
		event.register(BallistixMenuTypes.CONTAINER_RAILGUNTURRET.get(), ScreenRailgunTurret::new);
	}

	@SubscribeEvent
	public static void onModelEvent(ModelEvent.RegisterAdditional event) {
		event.register(MODEL_RADARDISH);
		event.register(MODEL_FIRECONTROLRADARDISH);
		event.register(MODEL_MISSILECLOSERANGE);
		event.register(MODEL_MISSILEMEDIUMRANGE);
		event.register(MODEL_MISSILELONGRANGE);
		event.register(MODEL_DARKMATTERSPHERE);
		event.register(MODEL_DARKMATTERDISK);
		event.register(MODEL_FIREBALL);
		event.register(MODEL_EMP);
		event.register(MODEL_BLACKHOLECUBE);
		event.register(MODEL_AAMISSILE);
		event.register(MODEL_SAMTURRET_BALLJOINT);
		event.register(MODEL_SAMTURRET_RAIL);
		event.register(MODEL_ESMTOWER);
		event.register(MODEL_CIWSTURRET_BALLJOINT);
		event.register(MODEL_CIWSTURRET_HEAD);
		event.register(MODEL_CIWSTURRET_BARREL);
		event.register(MODEL_LASERTURRET_BALLJOINT);
		event.register(MODEL_LASERTURRET_HEAD);
		event.register(MODEL_RAILGUNTURRET_BALLJOINT);
		event.register(MODEL_RAILGUNTURRET_HEAD);
	}

	@SubscribeEvent
	public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(BallistixEntities.ENTITY_EXPLOSIVE.get(), RenderExplosive::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_GRENADE.get(), RenderGrenade::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_BLAST.get(), RenderBlast::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_SHRAPNEL.get(), RenderShrapnel::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MISSILECR.get(), RenderMissile::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MISSILEMR.get(), RenderMissile::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MISSILELR.get(), RenderMissile::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_MINECART.get(), RenderMinecart::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_SAM.get(), RenderSAM::new);
		event.registerEntityRenderer(BallistixEntities.ENTITY_BULLET.get(), RenderBullet::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_MISSILESILO.get(), RenderMissileSilo::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_RADAR.get(), RenderRadar::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_FIRECONTROLRADAR.get(), RenderFireControlRadar::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_SAMTURRET.get(), RenderSAMTurret::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_ESMTOWER.get(), RenderESMTower::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_CIWSTURRET.get(), RenderCIWSTurret::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_LASERTURRET.get(), RenderLaserTurret::new);
		event.registerBlockEntityRenderer(BallistixTiles.TILE_RAILGUNTURRET.get(), RenderRailgunTurret::new);
	}

}
