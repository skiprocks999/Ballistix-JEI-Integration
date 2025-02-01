package ballistix.datagen.client;

import ballistix.References;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.item.ItemGrenade.SubtypeGrenade;
import ballistix.common.item.ItemMinecart.SubtypeMinecart;
import ballistix.registers.BallistixBlocks;
import ballistix.registers.BallistixItems;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
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
        layeredItem(BallistixItems.ITEM_BULLET, Parent.GENERATED, itemLoc(name(BallistixItems.ITEM_BULLET)));

        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo)).transforms()
                //
                .transform(ItemDisplayContext.GUI).rotation(20, 225, 0).translation(0, -1.5F, 0).scale(0.2F).end()
                //
                .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25F).end()
                //
                .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.40F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, 225, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.40F).end();


        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar), existingBlock(blockLoc("radarfull"))).transforms()
                //
                .transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, -1.5F, 0).scale(0.45F).end()
                //
                .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25F).end()
                //
                .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.40F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, 225, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.40F).end();

        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar), existingBlock(blockLoc("firecontrolradarfull"))).transforms()
                //
                .transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, -1.5F, 0).scale(0.45F).end()
                //
                .transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.25F).end()
                //
                .transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.5F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.40F).end()
                //
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, 225, 0).translation(0, 2.5F, 0).scale(0.375F).end()
                //
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.40F).end();

        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret), existingBlock(blockLoc("samturretitem")));
        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower), existingBlock(blockLoc("esmtower"))).transforms().transform(ItemDisplayContext.GUI).scale(0.3F).rotation(30.0F, 225.0F, 0.0F).end();
        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.ciwsturret), existingBlock(blockLoc("ciwsturretitem")));
        simpleBlockItem(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.laserturret), existingBlock(blockLoc("laserturretitem")));
    }

}
