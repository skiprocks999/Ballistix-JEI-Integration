package ballistix.datagen.client;

import ballistix.References;
import ballistix.common.item.ItemGrenade.SubtypeGrenade;
import ballistix.common.item.ItemMinecart.SubtypeMinecart;
import ballistix.registers.BallistixItems;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BallistixItemModelsProvider extends ElectrodynamicsItemModelsProvider {

	public BallistixItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, existingFileHelper, References.ID);
	}

	@Override
	protected void registerModels() {

		for (SubtypeGrenade grenade : SubtypeGrenade.values()) {
			layeredItem(BallistixItems.ITEMS_GRENADE.getValue(grenade), Parent.GENERATED, itemLoc("grenade/" + name(BallistixItems.ITEMS_GRENADE.getValue(grenade))));
		}

		for (SubtypeMinecart minecart : SubtypeMinecart.values()) {
			layeredItem(BallistixItems.ITEMS_MINECART.getValue(minecart), Parent.GENERATED, itemLoc("minecart/" + name(BallistixItems.ITEMS_MINECART.getValue(minecart))));

		}

		layeredItem(BallistixItems.ITEM_DEFUSER, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_DEFUSER)));
		layeredItem(BallistixItems.ITEM_DUSTPOISON, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_DUSTPOISON)));
		layeredItem(BallistixItems.ITEM_LASERDESIGNATOR, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_LASERDESIGNATOR)));
		layeredItem(BallistixItems.ITEM_RADARGUN, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_RADARGUN)));
		layeredItem(BallistixItems.ITEM_SCANNER, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_SCANNER)));

	}

}
