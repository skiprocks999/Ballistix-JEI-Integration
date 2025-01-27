package ballistix.datagen.client;

import ballistix.References;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.block.subtype.SubtypeBlast;
import ballistix.common.block.subtype.SubtypeMissile;
import ballistix.common.item.ItemGrenade.SubtypeGrenade;
import ballistix.common.item.ItemMinecart.SubtypeMinecart;
import ballistix.registers.BallistixBlocks;
import ballistix.registers.BallistixItems;
import ballistix.registers.BallistixSounds;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.Level;

public class BallistixLangKeyProvider extends ElectrodynamicsLangKeyProvider {

    public BallistixLangKeyProvider(PackOutput output, Locale locale) {
        super(output, locale, References.ID);
    }

    @Override
    protected void addTranslations() {

        switch (locale) {
            case EN_US:
            default:

                addCreativeTab("main", "Ballistix");

                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.obsidian), "Obsidian TNT");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.condensive), "Condensive Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.attractive), "Attractive Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.repulsive), "Repulsive Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.incendiary), "Incendiary Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.breaching), "Breaching Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.shrapnel), "Shrapnel Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.debilitation), "Debilitation Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.chemical), "Chemical Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.thermobaric), "Thermobaric Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.contagious), "Contagious Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.fragmentation), "Fragmentation Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.emp), "EMP Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.nuclear), "Nuclear Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.antimatter), "Antimatter Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.largeantimatter), "Large Antimatter Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.darkmatter), "Darkmatter Explosive");
                addBlock(BallistixBlocks.BLOCKS_EXPLOSIVE.getValue(SubtypeBlast.landmine), "Landmine");

                addBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.missilesilo), "Missile Silo");
                addBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.radar), "Radar");
                addBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.firecontrolradar), "Fire Control Radar");
                addBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.samturret), "SAM Turret");
                addBlock(BallistixBlocks.BLOCKS_BALLISTIXMACHINE.getValue(SubtypeBallistixMachine.esmtower), "ESM Tower");

                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.condensive), "Condensive Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.attractive), "Attractive Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.repulsive), "Repulsive Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.incendiary), "Incendiary Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.shrapnel), "Shrapnel Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.debilitation), "Debilitation Grenade");
                addItem(BallistixItems.ITEMS_GRENADE.getValue(SubtypeGrenade.chemical), "Chemical Grenade");

                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.obsidian), "Minecart with Obsidian TNT");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.condensive), "Minecart with Condensive Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.attractive), "Minecart with Attractive Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.repulsive), "Minecart with Repulsive Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.incendiary), "Minecart with Incendiary Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.breaching), "Minecart with Breaching Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.shrapnel), "Minecart with Shrapnel Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.debilitation), "Minecart with Debilitation Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.chemical), "Minecart with Chemical Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.thermobaric), "Minecart with Thermobaric Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.contagious), "Minecart with Contagious Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.emp), "Minecart with EMP Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.fragmentation), "Minecart with Fragmentation Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.nuclear), "Minecart with Nuclear Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.antimatter), "Minecart with Antimatter Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.largeantimatter), "Minecart with Large Antimatter Explosive");
                addItem(BallistixItems.ITEMS_MINECART.getValue(SubtypeMinecart.darkmatter), "Minecart with Darkmatter Explosive");

                addItem(BallistixItems.ITEM_DUSTPOISON, "Poison Dust");

                addItem(BallistixItems.ITEMS_MISSILE.getValue(SubtypeMissile.closerange), "Close-range Missile");
                addItem(BallistixItems.ITEMS_MISSILE.getValue(SubtypeMissile.mediumrange), "Medium-range Missile");
                addItem(BallistixItems.ITEMS_MISSILE.getValue(SubtypeMissile.longrange), "Long-range Missile");

                addItem(BallistixItems.ITEM_AAMISSILE, "Ballistic Rocket");

                addItem(BallistixItems.ITEM_ROCKETLAUNCHER, "Rocket Launcher");
                addItem(BallistixItems.ITEM_RADARGUN, "Radar Gun");
                addItem(BallistixItems.ITEM_LASERDESIGNATOR, "Laser Designator");
                addItem(BallistixItems.ITEM_TRACKER, "Tracker");
                addItem(BallistixItems.ITEM_SCANNER, "Scanner");
                addItem(BallistixItems.ITEM_DEFUSER, "Defuser");

                addContainer("missilesilo", "Missile Silo");
                addContainer("samturret", "SAM Turret");
                addContainer("searchradar", "Search Radar");
                addContainer("firecontrolradar", "Fire Control Radar");
                addContainer("esmtower", "ESM Tower");

                addGuiLabel("missilesilo.missile", "Missile");
                addGuiLabel("missilesilo.explosive", "Explosive");
                addGuiLabel("missilesilo.x", "X");
                addGuiLabel("missilesilo.y", "Y");
                addGuiLabel("missilesilo.z", "Z");
                addGuiLabel("missilesilo.freq", "" + '\u039b');
                addGuiLabel("missilesilo.sync", "Sync");

                addDamageSource("chemicalgas", "%s opened the Zyklon-B without reading the instructions!");
                addDamageSource("shrapnel", "%s was hit by shrapnel!");

                addChatMessage("radargun.text", "Coordinates: %s");
                addChatMessage("radargun.turretsucess", "Bound");
                addChatMessage("radargun.turrettoofar", "Turret too far away!");
                addChatMessage("laserdesignator.launch", "Launching all missiles with frequency %s to targets: ");
                addChatMessage("laserdesignator.launchsend", "-> %s");
                addChatMessage("laserdesignator.setfrequency", "Set frequency to: %s");
                addChatMessage("scanner.cleared", "Cleared trackers!");
                addChatMessage("scanner.none", "There were no trackers!");

                addTooltip("radargun.pos", "Stored: %s");
                addTooltip("radargun.notag", "No Coordinates Stored");
                addTooltip("laserdesignator.frequency", "Frequency: %s");
                addTooltip("laserdesignator.nofrequency", "Unbound");
                addTooltip("laserdesignator.invalidfreq", "Set a frequency for the silo");
                addTooltip("tracker.tracking", "Tracking: %s");
                addTooltip("tracker.none", "NONE");
                addTooltip("missile.range", "Range: %s Blocks");
                addTooltip("missile.unlimited", "Unlimited");

                addTooltip("turret.blockrange", "Block Range");
                addTooltip("turret.maxrange", "Max: %s");
                addTooltip("turret.minrange", "Min: %s");

                addTooltip("radar.frequencymanager", "Frequency Manager");
                addTooltip("radar.frequencymanager.delete", "Delete");

                addTooltip("radar.redstone", "Redstone");
                addTooltip("radar.redstone.enabled", "Enabled");
                addTooltip("radar.redstone.disabled", "Disabled");

                addGuiLabel("turret.radar", "Radar: ");
                addGuiLabel("turret.radarnone", "None");
                addGuiLabel("turret.status", "Status: %s");
                addGuiLabel("turret.statusunlinked", "Unlinked");
                addGuiLabel("turret.statusnopower", "No Power");
                addGuiLabel("turret.statuscooldown", "Cooldown %s");
                addGuiLabel("turret.statusnotarget", "No Target");
                addGuiLabel("turret.statusoutofrange", "Out of Range");
                addGuiLabel("turret.statusnoammo", "No Ammo");
                addGuiLabel("turret.statusgood", "Good");

                addGuiLabel("radar.tracking", "Tracking: ");
                addGuiLabel("radar.frequencywhitelist.mode", "Whitelist Mode");
                addGuiLabel("radar.frequencywhitelist.enabled", "Enabled");
                addGuiLabel("radar.frequencywhitelist.disabled", "Disabled");
                addGuiLabel("radar.frequencywhitelist.list", "Whitelisted");
                addGuiLabel("radar.frequencywhitelist.add", "Add");
                addGuiLabel("radar.bearingunknown", "Bearing Unknown");
                addGuiLabel("radar.bearing", "Bearing: %1$s" + '\u00B0' + " - %2$s" + '\u00B0');

                addGuiLabel("esmtower.nosearchradars", "No Search Radars Detected");
                addGuiLabel("esmtower.searchradardetected", "Search Radar Detected");
                addGuiLabel("esmtower.detectedfirecontrolradars", "Fire Control Radars");

                addSubtitle(BallistixSounds.SOUND_ANTIMATTEREXPLOSION, "Antimatter bomb detonates");
                addSubtitle(BallistixSounds.SOUND_DARKMATTER, "Dark matter bomb detonates");
                addSubtitle(BallistixSounds.SOUND_NUCLEAREXPLOSION, "Nuclear bomb detonates");
                addSubtitle(BallistixSounds.SOUND_EMPEXPLOSION, "EMP detonates");
                addSubtitle(BallistixSounds.SOUND_MISSILE_ROCKETLAUNCHER, "Missile is fired from rocket lancher");
                addSubtitle(BallistixSounds.SOUND_MISSILE_SILO, "Missile launches from silo");
                addSubtitle(BallistixSounds.SOUND_RADAR, "Radar pulses");
                addSubtitle(BallistixSounds.SOUND_FIRECONTROLRADAR, "Fire Control Radar tracks");

                addDimension(Level.OVERWORLD.location().getPath(), "The Overworld");
                addDimension(Level.NETHER.location().getPath(), "The Nether");
                addDimension(Level.END.location().getPath(), "The End");

                addGuidebook(References.ID, "Ballistix");

                addGuidebook("chapter.missilesilo", "Missile Silo");
                addGuidebook("chapter.missilesilo.l1", "The Missile Silo is used, as the name suggests, to launch various missiles with different types of warheads. The Silo has 3 available missile types with the following block ranges:");
                addGuidebook("chapter.missilesilo.range", "%1$s : %2$s");
                addGuidebook("chapter.missilesilo.close", "Close-Range");
                addGuidebook("chapter.missilesilo.medium", "Medium-Range");
                addGuidebook("chapter.missilesilo.long", "Long-Range");
                addGuidebook("chapter.missilesilo.unlimited", "Unlimited");
                addGuidebook("chapter.missilesilo.l2", "Each missile type is capable of carrying any explosive as a warhead.");

                addGuidebook("chapter.missilesilo.l3", "To load an explosive into a missile, first place the missile of your choice into the silo in its designated slot. Next, choose an explosive type and place it into its designated spot in the GUI. Next, program the target destination into the coordinate boxes. This can either be done manually or with a Radar Gun. Finally, " +
                        "once this is all completed, to fire the missile, apply a redstone signal to the silo.");

                addGuidebook("chapter.items", "Items");
                addGuidebook("chapter.items.rocketlauncher1", "The Rocket Launcher is capable of firing Close-Range missiles with any explosive warhead attached as a projectile. To fire it, have a Close-Range missile in your inventory along with the explosive type of your choice. Note, it must be a Ballistix explosive, and it must be the block form. Once this is done, hold " +
                        "the Launcher and Right-Click to fire!");

                addGuidebook("chapter.items.radargun1", "The Radar Gun is used to collect coordinates of a target and feed them into the Missile Silo instead of programming them manually. To use the Radar Gun, simply Right-Click on the target with the Gun to store its coordinates. This will expend %s and store the coordinates to the Gun. Then Right-Click on the Missile Silo " +
                        "with the Gun to feed in the coordinates. Alternatively, the coordinates can be imported using the \"Sync\" slot in the GUI.");

                addGuidebook("chapter.items.laserdesignator1", "The Laser Designator is used to launch missiles from a Missile Silo remotely. To use it, first place a Silo and prepare a missile with your choice of warhead. Next, Right-Click the Designator on the Silo to link the Designator to the Silo's frequency. Alternatively, the frequency can be linked via the \"Sync\" slot. " +
                        "Note, another the benefit of Designator is that you will not need to pre-program coordinates to the Missile Silo.");
                addGuidebook("chapter.items.laserdesignator2", "With the Silo prepared and the Designator linked to the Silo, all the remains is to find a suitable target. When you find one, simply Right-Click on it with the Designator. This will expend %s, automatically feed the target's coordinates into the Silo, and fire the missile. Note, make sure the target is actually " +
                        "in range of the missile in the Silo!");

                addGuidebook("chapter.items.defuser1", "The Defuser is your only hope of stopping an explosive from detonating once it has been activated. To use it, Right-Click on the explosive with the Defuser, and it will, as the name suggests, defuse it! This will expend %s and drop the explosive that was defused on the ground. Note, the Defuser also works on Vanilla TNT.");

                addGuidebook("chapter.items.tracker1", "The Tracker is capable of tracking the location of any entity in the world, player or otherwise. This is especially useful, as it allows you to track other players and send them Thermonuclear War...care packages from afar. To start tracking an entity, simply Right-Click on it with the Tracker. This will expend %s. Note the " +
                        "Tracker only keeps tabs on the X and Z coordinates of the entity along with what dimension they're in, meaning there is still a little guesswork involved. If the entity dies or the player disconnects from the server, the Tracker will stop tracking them!");

                addGuidebook("chapter.items.scanner1", "The Scanner is every paranoid player's best friend. Think someone has tagged you with a Tracker and is trying to nuke your prestigious dirt house? Fear not; Simply right-click with the Scanner. This will expend %s and jam any tracking signals if they are present!");

                addJei("info.item.missilecloserange", "Specs:\n    Range: 3000 Blocks");
                addJei("info.item.missilemediumrange", "Specs:\n    Range: 10 000 Blocks");
                addJei("info.item.missilelongrange", "Specs:\n    Range: Unlimited");

        }

    }

}
