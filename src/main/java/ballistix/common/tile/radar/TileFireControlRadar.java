package ballistix.common.tile.radar;

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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashSet;

public class TileFireControlRadar extends GenericTile {

    public double savedTickRotation;
    public double rotationSpeed;
    public boolean hasPower = false;
    private final AABB searchArea = new AABB(getBlockPos()).inflate(Constants.FIRE_CONTROL_RADAR_RANGE);
    @Nullable
    public EntityMissile tracking;
    public boolean redstone = false;

    public TileFireControlRadar(BlockPos pos, BlockState state) {
        super(BallistixTiles.TILE_FIRECONTROLRADAR.get(), pos, state);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickCommon(this::tickCommon).tickClient(this::tickClient));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentElectrodynamic(this, false, true).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).maxJoules(Constants.FIRE_CONTROL_RADAR_USAGE * 20));
    }

    public void tickServer(ComponentTickable tickable) {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        electro.joules(electro.getJoulesStored() - (Constants.RADAR_USAGE / 20.0));

        if (!hasPower || level.getBrightness(LightLayer.SKY, getBlockPos()) <= 0) {
            tracking = null;
            return;
        }

        if(tracking != null && tracking.isRemoved()) {
            tracking = null;
        }

        EntityMissile temp = null;

        for (EntityMissile missile : EntityMissile.MISSILES.getOrDefault(level.dimension(), new HashSet<>())) {
            if (missile.getBoundingBox().intersects(searchArea)) {
                if(temp == null) {
                    temp = missile;
                } else if (getDistanceToMissile(new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5), missile.getPosition()) < getDistanceToMissile(new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5), temp.getPosition())) {
                    temp = missile;
                }
            }
        }

        if(tracking == null) {
            tracking = temp;
        }

    }

    protected void tickCommon(ComponentTickable tickable) {

        savedTickRotation += rotationSpeed;

        hasPower = this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored() > 0;

        rotationSpeed = Mth.clamp(rotationSpeed + 0.25 * (hasPower ? 1 : -1), 0.0, 10.0);

    }

    public void tickClient(ComponentTickable tickable) {
        if (tickable.getTicks() % 50 == 0 && hasPower) {
            SoundAPI.playSound(BallistixSounds.SOUND_FIRECONTROLRADAR.get(), SoundSource.BLOCKS, 1.0F, 1.0F, worldPosition);
        }
    }

    public static double getDistanceToMissile(Vec3 pos, Vec3 missilePos) {
        double deltaX = missilePos.x - pos.x;
        double deltaY = missilePos.y - pos.y;
        double deltaZ = missilePos.z - pos.z;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    //will return negative one if can't hit;
    //otherwise returns the time in seconds
    public static double getTimeToIntercept(Vec3 missPos, Vec3 missVect, float missSpeed, float bulletSpeed, Vec3 interceptorPos) {
        Vec3 missVector = missVect.scale(missSpeed);

        double a = missVector.dot(missVector) - bulletSpeed * bulletSpeed; // if this is zero it means the proj can never catch the target

        if(a == 0) {
            return -1;
        }

        double b = missPos.dot(missVector) * 2;
        double c = missPos.dot(missPos);
        double root = (b * b) - 4 * a * c;
        if(root < 0) {

            return -1;

        } else if(root == 0) {

            return -b / (2 * a);

        } else {

            root = Math.sqrt(root);

            double sol1 = (-b - root) / (2 * a);
            double sol2 = (-b + root) / (2 * a);

            if(sol1 > 0 && sol2 > 0) {
                return  -1;
            } else if (sol1 > 0) {

                return -sol2;

            } else if (sol2 > 0) {

                return -sol1;

            } else {

                return -Math.max(sol1, sol2);

            }

        }
    }

}
