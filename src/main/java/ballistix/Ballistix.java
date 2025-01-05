package ballistix;

import ballistix.client.ClientRegister;
import ballistix.common.blast.thread.ThreadSimpleBlast;
import ballistix.common.block.BallistixVoxelShapes;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.settings.Constants;
import ballistix.common.tags.BallistixTags;
import ballistix.registers.UnifiedBallistixRegister;
import electrodynamics.prefab.configuration.ConfigurationHandler;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(References.ID)
@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class Ballistix {

	public Ballistix(IEventBus bus) {
		ConfigurationHandler.registerConfig(Constants.class);
		BallistixVoxelShapes.init();
		UnifiedBallistixRegister.register(bus);
		new ThreadSimpleBlast(null, BlockPos.ZERO, (int) Constants.EXPLOSIVE_ANTIMATTER_RADIUS, Integer.MAX_VALUE, null, true, SubtypeBlast.antimatter.ordinal()).start();
		new ThreadSimpleBlast(null, BlockPos.ZERO, (int) Constants.EXPLOSIVE_DARKMATTER_RADIUS, Integer.MAX_VALUE, null, true, SubtypeBlast.darkmatter.ordinal()).start();
		new ThreadSimpleBlast(null, BlockPos.ZERO, (int) Constants.EXPLOSIVE_LARGEANTIMATTER_RADIUS, Integer.MAX_VALUE, null, true, SubtypeBlast.largeantimatter.ordinal()).start();
		new ThreadSimpleBlast(null, BlockPos.ZERO, (int) Constants.EXPLOSIVE_NUCLEAR_SIZE * 2, Integer.MAX_VALUE, null, true, SubtypeBlast.nuclear.ordinal()).start();
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ClientRegister.setup();
		});
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		BallistixTags.init();
	}

}
