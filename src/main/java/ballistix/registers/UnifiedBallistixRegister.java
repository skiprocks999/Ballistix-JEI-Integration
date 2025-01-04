package ballistix.registers;

import ballistix.common.recipe.BallistixRecipeInit;
import net.neoforged.bus.api.IEventBus;

public class UnifiedBallistixRegister {

	public static void register(IEventBus bus) {
		BallistixAttachmentTypes.ATTACHMENT_TYPES.register(bus);
		BallistixDataComponentTypes.DATA_COMPONENT_TYPES.register(bus);
		BallistixBlocks.BLOCKS.register(bus);
		BallistixItems.ITEMS.register(bus);
		BallistixTiles.BLOCK_ENTITY_TYPES.register(bus);
		BallistixRecipeInit.INGREDIENT_TYPES.register(bus);
		BallistixMenuTypes.MENU_TYPES.register(bus);
		BallistixEntities.ENTITIES.register(bus);
		BallistixSounds.SOUNDS.register(bus);
		BallistixCreativeTabs.CREATIVE_TABS.register(bus);
	}

}
