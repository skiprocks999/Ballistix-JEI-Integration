package ballistix.common.tile;

import ballistix.common.entity.EntityMissile;
import ballistix.common.settings.Constants;
import ballistix.registers.BallistixSounds;
import ballistix.registers.BallistixTiles;
import electrodynamics.api.sound.SoundAPI;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;

public class TileRadar extends GenericTile {

    public double savedTickRotation;
    public double rotationSpeed;
    public boolean hasPower = false;
    private final AABB searchArea = new AABB(getBlockPos()).inflate(Constants.RADAR_RANGE);
    private final HashSet<EntityMissile> trackedMissiles = new HashSet<>();
    public boolean redstone = false;

    public TileRadar(BlockPos pos, BlockState state) {
        super(BallistixTiles.TILE_RADAR.get(), pos, state);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickCommon(this::tickCommon).tickClient(this::tickClient));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentElectrodynamic(this, false, true).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).maxJoules(Constants.RADAR_USAGE * 20));
    }

    public void tickServer(ComponentTickable tickable) {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        electro.joules(electro.getJoulesStored() - (Constants.RADAR_USAGE / 20.0));

        if (!hasPower || level.getBrightness(LightLayer.SKY, getBlockPos()) <= 0) {
            if (redstone) {
                redstone = false;
                level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            }
            return;
        }

        if (trackedMissiles.isEmpty() && redstone) {
            redstone = false;
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        } else if (!trackedMissiles.isEmpty() && !redstone) {
            redstone = true;
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }

        trackedMissiles.clear();

        if (!hasPower) {
            return;
        }

        for (EntityMissile missile : EntityMissile.MISSILES.getOrDefault(level.dimension(), new HashSet<>())) {
            if (missile.getBoundingBox().intersects(searchArea)) {
                trackedMissiles.add(missile);
            }
        }

    }

    protected void tickCommon(ComponentTickable tickable) {

        savedTickRotation += rotationSpeed;

        hasPower = this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored() > 0;

        rotationSpeed = Mth.clamp(rotationSpeed + 0.25 * (hasPower ? 1 : -1), 0.0, 10.0);

    }

    public void tickClient(ComponentTickable tickable) {
        if (tickable.getTicks() % 50 == 0 && hasPower) {
            SoundAPI.playSound(BallistixSounds.SOUND_RADAR.get(), SoundSource.BLOCKS, 1.0F, 1.0F, worldPosition);
        }
    }

    @Override
    public int getSignal(Direction dir) {
        return redstone ? 15 : 0;
    }
}
