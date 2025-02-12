package ballistix.common.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.common.inventory.container.ContainerESMTower;
import ballistix.common.settings.Constants;
import ballistix.common.tile.radar.TileFireControlRadar;
import ballistix.common.tile.radar.TileSearchRadar;
import ballistix.registers.BallistixTiles;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class TileESMTower extends GenericTile implements IMultiblockParentTile {

    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileSearchRadar>> SEARCH_RADARS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileFireControlRadar>> FIRE_CONTROL_RADARS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<ResourceKey<Level>, HashSet<TileESMTower>> ESM_TOWERS = new ConcurrentHashMap<>();

    public final Property<Boolean> active = property(new Property<>(PropertyTypes.BOOLEAN, "active", false));
    public final Property<Boolean> searchRadarDetected = property(new Property<>(PropertyTypes.BOOLEAN, "searchradar", false));
    public final Property<ArrayList<BlockPos>> fireControlRadars = property(new Property<>(PropertyTypes.BLOCK_POS_LIST, "firecontrolradars", new ArrayList<BlockPos>())).setNoUpdateServer();

    private final AABB searchArea = new AABB(getBlockPos()).inflate(Constants.ESM_TOWER_SEARCH_RADIUS);

    public TileESMTower(BlockPos worldPos, BlockState blockState) {
        super(BallistixTiles.TILE_ESMTOWER.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentElectrodynamic(this, false, true).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).maxJoules(Constants.ESM_TOWER_USAGE_PER_TICK * 20));
        addComponent(new ComponentContainerProvider("container.esmtower", this).createMenu((id, player) -> new ContainerESMTower(id, player, new SimpleContainer(0), getCoordsArray())));
    }

    public void tickServer(ComponentTickable tickable) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        active.set(electro.getJoulesStored() > Constants.ESM_TOWER_USAGE_PER_TICK && level.getBrightness(LightLayer.SKY, getBlockPos()) > 0);

        if(!active.get()) {
            removeESMTower(this);
            searchRadarDetected.set(false);
            fireControlRadars.get().clear();
            fireControlRadars.forceDirty();
            return;
        }

        addESMTower(this);

        searchRadarDetected.set(false);
        fireControlRadars.get().clear();

        for(TileSearchRadar radar : SEARCH_RADARS.getOrDefault(getLevel().dimension(), new HashSet<>())) {
            if(searchArea.intersects(new AABB(radar.getBlockPos()))) {
                searchRadarDetected.set(true);
                break;
            }
        }

        for(TileFireControlRadar radar : FIRE_CONTROL_RADARS.getOrDefault(getLevel().dimension(), new HashSet<>())) {
            if(searchArea.intersects(new AABB(radar.getBlockPos()))) {
                fireControlRadars.get().add(radar.getBlockPos());
            }
        }

        fireControlRadars.forceDirty();


    }

    @Override
    public IMultiblockParentBlock.SubnodeWrapper getSubNodes() {
        return SubtypeBallistixMachine.Subnodes.ESM_TOWER;
    }

    @Override
    public InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
        return useWithoutItem(player, hit);
    }

    @Override
    public void onSubnodeDestroyed(TileMultiSubnode tileMultiSubnode) {
        level.destroyBlock(worldPosition, true);
    }

    @Override
    public Direction getFacingDirection() {
        return getFacing();
    }

    public static void removeSearchRadar(TileSearchRadar radar) {
        SEARCH_RADARS.getOrDefault(radar.getLevel().dimension(), new HashSet<>()).remove(radar);
    }

    public static void removeFireControlRadar(TileFireControlRadar radar) {
        FIRE_CONTROL_RADARS.getOrDefault(radar.getLevel().dimension(), new HashSet<>()).remove(radar);
    }

    public static void removeESMTower(TileESMTower esm) {
        ESM_TOWERS.getOrDefault(esm.getLevel().dimension(), new HashSet<>()).remove(esm);
    }

    public static void addSearchRadar(TileSearchRadar radar) {
        HashSet<TileSearchRadar> radars = SEARCH_RADARS.getOrDefault(radar.getLevel().dimension(), new HashSet<>());
        radars.add(radar);
        SEARCH_RADARS.put(radar.getLevel().dimension(), radars);
    }

    public static void addFireControlRadar(TileFireControlRadar radar) {
        HashSet<TileFireControlRadar> radars = FIRE_CONTROL_RADARS.getOrDefault(radar.getLevel().dimension(), new HashSet<>());
        radars.add(radar);
        FIRE_CONTROL_RADARS.put(radar.getLevel().dimension(), radars);
    }

    public static void addESMTower(TileESMTower esm) {
        HashSet<TileESMTower> esmTowers = ESM_TOWERS.getOrDefault(esm.getLevel().dimension(), new HashSet<>());
        esmTowers.add(esm);
        ESM_TOWERS.put(esm.getLevel().dimension(), esmTowers);
    }

}
