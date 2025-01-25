package ballistix.common.block.subtype;

import ballistix.common.block.BallistixVoxelShapes;
import ballistix.common.tile.radar.TileFireControlRadar;
import ballistix.common.tile.TileMissileSilo;
import ballistix.common.tile.TileRadar;
import ballistix.common.tile.turret.antimissile.TileTurretSAM;
import electrodynamics.api.ISubtype;
import electrodynamics.api.multiblock.subnodebased.Subnode;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.api.tile.IMachine;
import electrodynamics.api.tile.MachineProperties;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public enum SubtypeBallistixMachine implements ISubtype, IMachine {

    missilesilo(true,TileMissileSilo::new, MachineProperties.builder().setShapeProvider(BallistixVoxelShapes.MISSILE_SILO).setSubnodes(Subnodes.MISSILE_SILO)),
    radar(true, TileRadar::new, MachineProperties.builder().setShapeProvider(BallistixVoxelShapes.RADAR)),
    firecontrolradar(true, TileFireControlRadar::new, MachineProperties.builder().setShapeProvider(BallistixVoxelShapes.FIRE_CONTROL_RADAR)),
    samturret(true, TileTurretSAM::new)
    ;

    private final BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier;
    private final boolean showInItemGroup;
    private final MachineProperties properties;

    private SubtypeBallistixMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        this(showInItemGroup, blockEntitySupplier, MachineProperties.DEFAULT);
    }

    private SubtypeBallistixMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier, MachineProperties properties) {
        this.showInItemGroup = showInItemGroup;
        this.blockEntitySupplier = blockEntitySupplier;
        this.properties = properties;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntity> getBlockEntitySupplier() {
        return blockEntitySupplier;
    }

    @Override
    public int getLitBrightness() {
        return properties.litBrightness;
    }

    @Override
    public RenderShape getRenderShape() {
        return properties.renderShape;
    }

    @Override
    public boolean isMultiblock() {
        return properties.isMultiblock;
    }

    @Override
    public boolean propegatesLightDown() {
        return properties.propegatesLightDown;
    }

    @Override
    public String tag() {
        return name();
    }

    @Override
    public String forgeTag() {
        return tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    public boolean isPlayerStorable() {
        return false;
    }

    @Override
    public IMultiblockParentBlock.SubnodeWrapper getSubnodes() {
        return properties.wrapper;
    }

    @Override
    public VoxelShapeProvider getVoxelShapeProvider() {
        return properties.provider;
    }

    @Override
    public boolean usesLit() {
        return properties.usesLit;
    }

    public boolean showInItemGroup() {
        return showInItemGroup;
    }


    public static class Subnodes {

        public static final IMultiblockParentBlock.SubnodeWrapper MISSILE_SILO = make(() -> {

            //

            Subnode[] subnodesSouth = new Subnode[14];

            Subnode[] subnodesNorth = new Subnode[14];

            Subnode[] subnodesEast = new Subnode[14];

            Subnode[] subnodesWest = new Subnode[14];

            VoxelShape[] shapes = new VoxelShape[14];

            // Hand-coded but required :|
            // And yes if it looks painful its because it was :D

            /* First Layer */
            shapes[0] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(1, 1, 1, 7, 2, 16),
                    //
                    Block.box(7, 1, 1, 16, 2, 7),
                    //
                    Block.box(7, 2, 15, 9.5, 4, 16),
                    //
                    Block.box(8, 2, 14, 10.5, 4, 15),
                    //
                    Block.box(9, 2, 13, 11.5, 4, 14),
                    //
                    Block.box(10, 2, 12, 12.5, 4, 13),
                    //
                    Block.box(11, 2, 11, 13.5, 4, 12),
                    //
                    Block.box(12, 2, 10, 14.5, 4, 11),
                    //
                    Block.box(13, 2, 9, 15.5, 4, 10),
                    //
                    Block.box(14, 2, 8, 16, 4, 9),
                    //
                    Block.box(15, 2, 7, 16, 4, 8),
                    //
                    Block.box(7, 8, 15, 9.5, 10, 16),
                    //
                    Block.box(8, 8, 14, 10.5, 10, 15),
                    //
                    Block.box(9, 8, 13, 11.5, 10, 14),
                    //
                    Block.box(10, 8, 12, 12.5, 10, 13),
                    //
                    Block.box(11, 8, 11, 13.5, 10, 12),
                    //
                    Block.box(12, 8, 10, 14.5, 10, 11),
                    //
                    Block.box(13, 8, 9, 15.5, 10, 10),
                    //
                    Block.box(14, 8, 8, 16, 10, 9),
                    //
                    Block.box(15, 8, 7, 16, 10, 8),
                    //
                    Block.box(7, 14, 15, 9.5, 16, 16),
                    //
                    Block.box(8, 14, 14, 10.5, 16, 15),
                    //
                    Block.box(9, 14, 13, 11.5, 16, 14),
                    //
                    Block.box(10, 14, 12, 12.5, 16, 13),
                    //
                    Block.box(11, 14, 11, 13.5, 16, 12),
                    //
                    Block.box(12, 14, 10, 14.5, 16, 11),
                    //
                    Block.box(13, 14, 9, 15.5, 16, 10),
                    //
                    Block.box(14, 14, 8, 16, 16, 9),
                    //
                    Block.box(15, 14, 7, 16, 16, 8)
                    //
            );

            shapes[1] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(0, 1, 1, 16, 2, 7),
                    //
                    Block.box(0, 2, 8, 0.5, 4, 9),
                    //
                    Block.box(0, 2, 7, 1.5, 4, 8),
                    //
                    Block.box(0, 2, 6, 2.5, 4, 7),
                    //
                    Block.box(1, 2, 5, 3.5, 4, 6),
                    //
                    Block.box(2, 2, 4, 4.5, 4, 5),
                    //
                    Block.box(3, 2, 3, 5.5, 4, 4),
                    //
                    Block.box(4, 2, 2, 6.5, 4, 3),
                    //
                    Block.box(0, 8, 8, 0.5, 10, 9),
                    //
                    Block.box(0, 8, 7, 1.5, 10, 8),
                    //
                    Block.box(0, 8, 6, 2.5, 10, 7),
                    //
                    Block.box(1, 8, 5, 3.5, 10, 6),
                    //
                    Block.box(2, 8, 4, 4.5, 10, 5),
                    //
                    Block.box(3, 8, 3, 5.5, 10, 4),
                    //
                    Block.box(4, 8, 2, 6.5, 10, 3),
                    //
                    Block.box(0, 14, 8, 0.5, 16, 9),
                    //
                    Block.box(0, 14, 7, 1.5, 16, 8),
                    //
                    Block.box(0, 14, 6, 2.5, 16, 7),
                    //
                    Block.box(1, 14, 5, 3.5, 16, 6),
                    //
                    Block.box(2, 14, 4, 4.5, 16, 5),
                    //
                    Block.box(3, 14, 3, 5.5, 16, 4),
                    //
                    Block.box(4, 14, 2, 6.5, 16, 3),
                    //
                    Block.box(15.8, 2, 8, 16, 4, 9),
                    //
                    Block.box(14.8, 2, 7, 16, 4, 8),
                    //
                    Block.box(13.8, 2, 6, 16, 4, 7),
                    //
                    Block.box(12.8, 2, 5, 15.3, 4, 6),
                    //
                    Block.box(11.8, 2, 4, 14.3, 4, 5),
                    //
                    Block.box(10.8, 2, 3, 13.3, 4, 4),
                    //
                    Block.box(9.8, 2, 2, 12.3, 4, 3),
                    //
                    Block.box(8.8, 2, 1, 11.3, 4, 2),
                    //
                    Block.box(15.8, 8, 8, 16, 10, 9),
                    //
                    Block.box(14.8, 8, 7, 16, 10, 8),
                    //
                    Block.box(13.8, 8, 6, 16, 10, 7),
                    //
                    Block.box(12.8, 8, 5, 15.3, 10, 6),
                    //
                    Block.box(11.8, 8, 4, 14.3, 10, 5),
                    //
                    Block.box(10.8, 8, 3, 13.3, 10, 4),
                    //
                    Block.box(9.8, 8, 2, 12.3, 10, 3),
                    //
                    Block.box(8.8, 8, 1, 11.3, 10, 2),
                    //
                    Block.box(15.8, 14, 8, 16, 16, 9),
                    //
                    Block.box(14.8, 14, 7, 16, 16, 8),
                    //
                    Block.box(13.8, 14, 6, 16, 16, 7),
                    //
                    Block.box(12.8, 14, 5, 15.3, 16, 6),
                    //
                    Block.box(11.8, 14, 4, 14.3, 16, 5),
                    //
                    Block.box(10.8, 14, 3, 13.3, 16, 4),
                    //
                    Block.box(9.8, 14, 2, 12.3, 16, 3),
                    //
                    Block.box(8.8, 14, 1, 11.3, 16, 2),
                    //
                    Block.box(5, 0, 0, 11, 16, 4)
                    //
            );

            shapes[2] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(9, 1, 1, 15, 2, 16),
                    //
                    Block.box(0, 1, 1, 9, 2, 7),
                    //
                    Block.box(6.8, 2, 15, 9.3, 4, 16),
                    //
                    Block.box(5.8, 2, 14, 8.3, 4, 15),
                    //
                    Block.box(4.8, 2, 13, 7.3, 4, 14),
                    //
                    Block.box(3.8, 2, 12, 6.3, 4, 13),
                    //
                    Block.box(2.8, 2, 11, 5.3, 4, 12),
                    //
                    Block.box(1.8, 2, 10, 4.3, 4, 11),
                    //
                    Block.box(0.8, 2, 9, 3.3, 4, 10),
                    //
                    Block.box(0, 2, 8, 2.3, 4, 9),
                    //
                    Block.box(0, 2, 7, 1.3, 4, 8),
                    //
                    Block.box(0, 2, 6, 0.3, 4, 7),
                    //
                    Block.box(6.8, 8, 15, 9.3, 10, 16),
                    //
                    Block.box(5.8, 8, 14, 8.3, 10, 15),
                    //
                    Block.box(4.8, 8, 13, 7.3, 10, 14),
                    //
                    Block.box(3.8, 8, 12, 6.3, 10, 13),
                    //
                    Block.box(2.8, 8, 11, 5.3, 10, 12),
                    //
                    Block.box(1.8, 8, 10, 4.3, 10, 11),
                    //
                    Block.box(0.8, 8, 9, 3.3, 10, 10),
                    //
                    Block.box(0, 8, 8, 2.3, 10, 9),
                    //
                    Block.box(0, 8, 7, 1.3, 10, 8),
                    //
                    Block.box(0, 8, 6, 0.3, 10, 7),
                    //
                    Block.box(6.8, 14, 15, 9.3, 16, 16),
                    //
                    Block.box(5.8, 14, 14, 8.3, 16, 15),
                    //
                    Block.box(4.8, 14, 13, 7.3, 16, 14),
                    //
                    Block.box(3.8, 14, 12, 6.3, 16, 13),
                    //
                    Block.box(2.8, 14, 11, 5.3, 16, 12),
                    //
                    Block.box(1.8, 14, 10, 4.3, 16, 11),
                    //
                    Block.box(0.8, 14, 9, 3.3, 16, 10),
                    //
                    Block.box(0, 14, 8, 2.3, 16, 9),
                    //
                    Block.box(0, 14, 7, 1.3, 16, 8),
                    //
                    Block.box(0, 14, 6, 0.3, 16, 7)
                    //
            );

            shapes[3] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(1, 1, 0, 7, 2, 16),
                    //
                    Block.box(6, 2, 0, 8.5, 4, 1),
                    //
                    Block.box(5, 2, 1, 7.5, 4, 2),
                    //
                    Block.box(4, 2, 2, 6.5, 4, 3),
                    //
                    Block.box(3, 2, 3, 5.5, 4, 4),
                    //
                    Block.box(2, 2, 4, 4.5, 4, 5),
                    //
                    Block.box(1, 2, 5, 3.5, 4, 6),
                    //
                    Block.box(6, 8, 0, 8.5, 10, 1),
                    //
                    Block.box(5, 8, 1, 7.5, 10, 2),
                    //
                    Block.box(4, 8, 2, 6.5, 10, 3),
                    //
                    Block.box(3, 8, 3, 5.5, 10, 4),
                    //
                    Block.box(2, 8, 4, 4.5, 10, 5),
                    //
                    Block.box(1, 8, 5, 3.5, 10, 6),
                    //
                    Block.box(6, 14, 0, 8.5, 16, 1),
                    //
                    Block.box(5, 14, 1, 7.5, 16, 2),
                    //
                    Block.box(4, 14, 2, 6.5, 16, 3),
                    //
                    Block.box(3, 14, 3, 5.5, 16, 4),
                    //
                    Block.box(2, 14, 4, 4.5, 16, 5),
                    //
                    Block.box(1, 14, 5, 3.5, 16, 6),
                    //
                    Block.box(0, 0, 5, 3, 16, 11),
                    //
                    Block.box(3, 0, 5.8, 4, 15.5, 10.2),
                    //
                    Block.box(10, 0, 6, 14, 16, 10)
                    //
            );

            shapes[4] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(9, 0, 1, 15, 2, 16),
                    //
                    Block.box(7.8, 2, 0, 10.3, 4, 1),
                    //
                    Block.box(8.8, 2, 1, 11.3, 4, 2),
                    //
                    Block.box(9.8, 2, 2, 12.3, 4, 3),
                    //
                    Block.box(10.8, 2, 3, 13.3, 4, 4),
                    //
                    Block.box(11.8, 2, 4, 14.3, 4, 5),
                    //
                    Block.box(12.8, 2, 5, 15.3, 4, 6),
                    //
                    Block.box(7.8, 8, 0, 10.3, 10, 1),
                    //
                    Block.box(8.8, 8, 1, 11.3, 10, 2),
                    //
                    Block.box(9.8, 8, 2, 12.3, 10, 3),
                    //
                    Block.box(10.8, 8, 3, 13.3, 10, 4),
                    //
                    Block.box(11.8, 8, 4, 14.3, 10, 5),
                    //
                    Block.box(12.8, 8, 5, 15.3, 10, 6),
                    //
                    Block.box(7.8, 14, 0, 10.3, 16, 1),
                    //
                    Block.box(8.8, 14, 1, 11.3, 16, 2),
                    //
                    Block.box(9.8, 14, 2, 12.3, 16, 3),
                    //
                    Block.box(10.8, 14, 3, 13.3, 16, 4),
                    //
                    Block.box(11.8, 14, 4, 14.3, 16, 5),
                    //
                    Block.box(12.8, 14, 5, 15.3, 16, 6),
                    //
                    Block.box(13, 0, 5, 16, 16, 11),
                    //
                    Block.box(12, 0, 5.8, 13, 15.5, 10.2),
                    //
                    Block.box(2, 0, 6, 6, 16, 10)
                    //
            );

            shapes[5] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(1, 1, 0, 7, 2, 15),
                    //
                    Block.box(7, 1, 9, 16, 2, 15)
                    //
            );

            shapes[6] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(0, 1, 9, 16, 2, 15),
                    //
                    Block.box(2, 1, 6.5, 13, 10, 14)
                    //
            );

            shapes[7] = Shapes.or(
                    //
                    Block.box(0, 0, 0, 16, 1, 16),
                    //
                    Block.box(9, 1, 1, 15, 2, 15),
                    //
                    Block.box(0, 1, 9, 9, 2, 15)
                    //
            );

            /* 2nd Layer */

            shapes[8] = Shapes.or(
                    //
                    Block.box(7, 4, 15, 9.5, 6, 16),
                    //
                    Block.box(8, 4, 14, 10.5, 6, 15),
                    //
                    Block.box(9, 4, 13, 11.5, 6, 14),
                    //
                    Block.box(10, 4, 12, 12.5, 6, 13),
                    //
                    Block.box(11, 4, 11, 13.5, 6, 12),
                    //
                    Block.box(12, 4, 10, 14.5, 6, 11),
                    //
                    Block.box(13, 4, 9, 15.5, 6, 10),
                    //
                    Block.box(14, 4, 8, 16, 6, 9),
                    //
                    Block.box(15, 4, 7, 16, 6, 8),
                    //
                    Block.box(7, 10, 15, 9.5, 12, 16),
                    //
                    Block.box(8, 10, 14, 10.5, 12, 15),
                    //
                    Block.box(9, 10, 13, 11.5, 12, 14),
                    //
                    Block.box(10, 10, 12, 12.5, 12, 13),
                    //
                    Block.box(11, 10, 11, 13.5, 12, 12),
                    //
                    Block.box(12, 10, 10, 14.5, 12, 11),
                    //
                    Block.box(13, 10, 9, 15.5, 12, 10),
                    //
                    Block.box(14, 10, 8, 16, 12, 9),
                    //
                    Block.box(15, 10, 7, 16, 12, 8)
                    //
            );

            shapes[9] = Shapes.or(
                    //
                    Block.box(0, 4, 8, 0.5, 6, 9),
                    //
                    Block.box(0, 4, 7, 1.5, 6, 8),
                    //
                    Block.box(0, 4, 6, 2.5, 6, 7),
                    //
                    Block.box(1, 4, 5, 3.5, 6, 6),
                    //
                    Block.box(2, 4, 4, 4.5, 6, 5),
                    //
                    Block.box(3, 4, 3, 5.5, 6, 4),
                    //
                    Block.box(4, 4, 2, 6.5, 6, 3),
                    //
                    Block.box(0, 10, 8, 0.5, 12, 9),
                    //
                    Block.box(0, 10, 7, 1.5, 12, 8),
                    //
                    Block.box(0, 10, 6, 2.5, 12, 7),
                    //
                    Block.box(1, 10, 5, 3.5, 12, 6),
                    //
                    Block.box(2, 10, 4, 4.5, 12, 5),
                    //
                    Block.box(3, 10, 3, 5.5, 12, 4),
                    //
                    Block.box(4, 10, 2, 6.5, 12, 3),
                    //
                    Block.box(15.8, 10, 8, 16, 12, 9),
                    //
                    Block.box(14.8, 10, 7, 16, 12, 8),
                    //
                    Block.box(13.8, 10, 6, 16, 12, 7),
                    //
                    Block.box(12.8, 10, 5, 15.3, 12, 6),
                    //
                    Block.box(11.8, 10, 4, 14.3, 12, 5),
                    //
                    Block.box(10.8, 10, 3, 13.3, 12, 4),
                    //
                    Block.box(9.8, 10, 2, 12.3, 12, 3),
                    //
                    Block.box(8.8, 10, 1, 11.3, 12, 2),
                    //
                    Block.box(15.8, 4, 8, 16, 6, 9),
                    //
                    Block.box(14.8, 4, 7, 16, 6, 8),
                    //
                    Block.box(13.8, 4, 6, 16, 6, 7),
                    //
                    Block.box(12.8, 4, 5, 15.3, 6, 6),
                    //
                    Block.box(11.8, 4, 4, 14.3, 6, 5),
                    //
                    Block.box(10.8, 4, 3, 13.3, 6, 4),
                    //
                    Block.box(9.8, 4, 2, 12.3, 6, 3),
                    //
                    Block.box(8.8, 4, 1, 11.3, 6, 2),
                    //
                    Block.box(5, 0, 0, 11, 16, 4)
                    //
            );

            shapes[10] = Shapes.or(
                    //
                    Block.box(6.8, 4, 15, 9.3, 6, 16),
                    //
                    Block.box(5.8, 4, 14, 8.3, 6, 15),
                    //
                    Block.box(4.8, 4, 13, 7.3, 6, 14),
                    //
                    Block.box(3.8, 4, 12, 6.3, 6, 13),
                    //
                    Block.box(2.8, 4, 11, 5.3, 6, 12),
                    //
                    Block.box(1.8, 4, 10, 4.3, 6, 11),
                    //
                    Block.box(0.8, 4, 9, 3.3, 6, 10),
                    //
                    Block.box(0, 4, 8, 2.3, 6, 9),
                    //
                    Block.box(0, 4, 7, 1.3, 6, 8),
                    //
                    Block.box(0, 4, 6, 0.3, 6, 7),
                    //
                    Block.box(6.8, 10, 15, 9.3, 12, 16),
                    //
                    Block.box(5.8, 10, 14, 8.3, 12, 15),
                    //
                    Block.box(4.8, 10, 13, 7.3, 12, 14),
                    //
                    Block.box(3.8, 10, 12, 6.3, 12, 13),
                    //
                    Block.box(2.8, 10, 11, 5.3, 12, 12),
                    //
                    Block.box(1.8, 10, 10, 4.3, 12, 11),
                    //
                    Block.box(0.8, 10, 9, 3.3, 12, 10),
                    //
                    Block.box(0, 10, 8, 2.3, 12, 9),
                    //
                    Block.box(0, 10, 7, 1.3, 12, 8),
                    //
                    Block.box(0, 10, 6, 0.3, 12, 7)
                    //
            );

            shapes[11] = Shapes.or(
                    //
                    Block.box(6, 4, 0, 8.5, 6, 1),
                    //
                    Block.box(5, 4, 1, 7.5, 6, 2),
                    //
                    Block.box(4, 4, 2, 6.5, 6, 3),
                    //
                    Block.box(3, 4, 3, 5.5, 6, 4),
                    //
                    Block.box(2, 4, 4, 4.5, 6, 5),
                    //
                    Block.box(1, 4, 5, 3.5, 6, 6),
                    //
                    Block.box(6, 10, 0, 8.5, 12, 1),
                    //
                    Block.box(5, 10, 1, 7.5, 12, 2),
                    //
                    Block.box(4, 10, 2, 6.5, 12, 3),
                    //
                    Block.box(3, 10, 3, 5.5, 12, 4),
                    //
                    Block.box(2, 10, 4, 4.5, 12, 5),
                    //
                    Block.box(1, 10, 5, 3.5, 12, 6),
                    //
                    Block.box(0, 0, 5, 3, 16, 11),
                    //
                    Block.box(3, 0, 5.8, 4, 15.5, 10.2),
                    //
                    Block.box(10, 0, 6, 14, 7, 10),
                    //
                    Block.box(14, 1.3, 7, 16, 2.8, 9)
                    //
            );

            shapes[12] = Shapes.or(
                    //
                    Block.box(0, 1.3, 7, 5, 2.8, 9),
                    //
                    Block.box(11, 1.3, 7, 16, 2.8, 9)
                    //
            );

            shapes[13] = Shapes.or(
                    //
                    Block.box(7.8, 4, 0, 10.3, 6, 1),
                    //
                    Block.box(8.8, 4, 1, 11.3, 6, 2),
                    //
                    Block.box(9.8, 4, 2, 12.3, 6, 3),
                    //
                    Block.box(10.8, 4, 3, 13.3, 6, 4),
                    //
                    Block.box(11.8, 4, 4, 14.3, 6, 5),
                    //
                    Block.box(12.8, 4, 5, 15.3, 6, 6),
                    //
                    Block.box(7.8, 10, 0, 10.3, 12, 1),
                    //
                    Block.box(8.8, 10, 1, 11.3, 12, 2),
                    //
                    Block.box(9.8, 10, 2, 12.3, 12, 3),
                    //
                    Block.box(10.8, 10, 3, 13.3, 12, 4),
                    //
                    Block.box(11.8, 10, 4, 14.3, 12, 5),
                    //
                    Block.box(12.8, 10, 5, 15.3, 12, 6),
                    //
                    Block.box(13, 0, 5, 16, 16, 11),
                    //
                    Block.box(12, 0, 5.8, 13, 15.5, 10.2),
                    //
                    Block.box(2, 0, 6, 6, 7, 10),
                    //
                    Block.box(0, 1.3, 7, 2, 2.8, 9)
                    //
            );

            /* SOUTH */

            // 1st layer
            subnodesSouth[0] = new Subnode(new BlockPos(-1, 0, -1), shapes[0]);
            subnodesSouth[1] = new Subnode(new BlockPos(0, 0, -1), shapes[1]);
            subnodesSouth[2] = new Subnode(new BlockPos(1, 0, -1), shapes[2]);
            subnodesSouth[3] = new Subnode(new BlockPos(-1, 0, 0), shapes[3]);
            subnodesSouth[4] = new Subnode(new BlockPos(1, 0, 0), shapes[4]);
            subnodesSouth[5] = new Subnode(new BlockPos(-1, 0, 1), shapes[5]);
            subnodesSouth[6] = new Subnode(new BlockPos(0, 0, 1), shapes[6]);
            subnodesSouth[7] = new Subnode(new BlockPos(1, 0, 1), shapes[7]);

            // 2nd layer
            subnodesSouth[8] = new Subnode(new BlockPos(-1, 1, -1), shapes[8]);
            subnodesSouth[9] = new Subnode(new BlockPos(0, 1, -1), shapes[9]);
            subnodesSouth[10] = new Subnode(new BlockPos(1, 1, -1), shapes[10]);
            subnodesSouth[11] = new Subnode(new BlockPos(-1, 1, 0), shapes[11]);
            subnodesSouth[12] = new Subnode(new BlockPos(0, 1, 0), shapes[12]);
            subnodesSouth[13] = new Subnode(new BlockPos(1, 1, 0), shapes[13]);

            /* NORTH */

            // 1st layer
            subnodesNorth[0] = new Subnode(new BlockPos(-1, 0, -1), rotate(Direction.NORTH, shapes[7]));
            subnodesNorth[1] = new Subnode(new BlockPos(0, 0, -1), rotate(Direction.NORTH, shapes[6]));
            subnodesNorth[2] = new Subnode(new BlockPos(1, 0, -1), rotate(Direction.NORTH, shapes[5]));
            subnodesNorth[3] = new Subnode(new BlockPos(-1, 0, 0), rotate(Direction.NORTH, shapes[4]));
            subnodesNorth[4] = new Subnode(new BlockPos(1, 0, 0), rotate(Direction.NORTH, shapes[3]));
            subnodesNorth[5] = new Subnode(new BlockPos(-1, 0, 1), rotate(Direction.NORTH, shapes[2]));
            subnodesNorth[6] = new Subnode(new BlockPos(0, 0, 1), rotate(Direction.NORTH, shapes[1]));
            subnodesNorth[7] = new Subnode(new BlockPos(1, 0, 1), rotate(Direction.NORTH, shapes[0]));

            // 2nd layer
            subnodesNorth[8] = new Subnode(new BlockPos(-1, 1, 0), rotate(Direction.NORTH, shapes[13]));
            subnodesNorth[9] = new Subnode(new BlockPos(0, 1, 0), rotate(Direction.NORTH, shapes[12]));
            subnodesNorth[10] = new Subnode(new BlockPos(1, 1, 0), rotate(Direction.NORTH, shapes[11]));
            subnodesNorth[11] = new Subnode(new BlockPos(-1, 1, 1), rotate(Direction.NORTH, shapes[10]));
            subnodesNorth[12] = new Subnode(new BlockPos(0, 1, 1), rotate(Direction.NORTH, shapes[9]));
            subnodesNorth[13] = new Subnode(new BlockPos(1, 1, 1), rotate(Direction.NORTH, shapes[8]));

            /* EAST */

            // 1st layer
            subnodesEast[0] = new Subnode(new BlockPos(-1, 0, -1), rotate(Direction.EAST, shapes[2]));
            subnodesEast[1] = new Subnode(new BlockPos(-1, 0, 0), rotate(Direction.EAST, shapes[1]));
            subnodesEast[2] = new Subnode(new BlockPos(-1, 0, 1), rotate(Direction.EAST, shapes[0]));
            subnodesEast[3] = new Subnode(new BlockPos(0, 0, -1), rotate(Direction.EAST, shapes[4]));
            subnodesEast[4] = new Subnode(new BlockPos(0, 0, 1), rotate(Direction.EAST, shapes[3]));
            subnodesEast[5] = new Subnode(new BlockPos(1, 0, -1), rotate(Direction.EAST, shapes[7]));
            subnodesEast[6] = new Subnode(new BlockPos(1, 0, 0), rotate(Direction.EAST, shapes[6]));
            subnodesEast[7] = new Subnode(new BlockPos(1, 0, 1), rotate(Direction.EAST, shapes[5]));

            // 2nd layer
            subnodesEast[8] = new Subnode(new BlockPos(-1, 1, -1), rotate(Direction.EAST, shapes[10]));
            subnodesEast[9] = new Subnode(new BlockPos(-1, 1, 0), rotate(Direction.EAST, shapes[9]));
            subnodesEast[10] = new Subnode(new BlockPos(-1, 1, 1), rotate(Direction.EAST, shapes[8]));
            subnodesEast[11] = new Subnode(new BlockPos(0, 1, -1), rotate(Direction.EAST, shapes[13]));
            subnodesEast[12] = new Subnode(new BlockPos(0, 1, 0), rotate(Direction.EAST, shapes[12]));
            subnodesEast[13] = new Subnode(new BlockPos(0, 1, 1), rotate(Direction.EAST, shapes[11]));

            /* WEST */

            // 1st layer
            subnodesWest[0] = new Subnode(new BlockPos(-1, 0, -1), rotate(Direction.WEST, shapes[5]));
            subnodesWest[1] = new Subnode(new BlockPos(-1, 0, 0), rotate(Direction.WEST, shapes[6]));
            subnodesWest[2] = new Subnode(new BlockPos(-1, 0, 1), rotate(Direction.WEST, shapes[7]));
            subnodesWest[3] = new Subnode(new BlockPos(0, 0, -1), rotate(Direction.WEST, shapes[3]));
            subnodesWest[4] = new Subnode(new BlockPos(0, 0, 1), rotate(Direction.WEST, shapes[4]));
            subnodesWest[5] = new Subnode(new BlockPos(1, 0, -1), rotate(Direction.WEST, shapes[0]));
            subnodesWest[6] = new Subnode(new BlockPos(1, 0, 0), rotate(Direction.WEST, shapes[1]));
            subnodesWest[7] = new Subnode(new BlockPos(1, 0, 1), rotate(Direction.WEST, shapes[2]));

            // 2nd layer
            subnodesWest[8] = new Subnode(new BlockPos(0, 1, -1), rotate(Direction.WEST, shapes[11]));
            subnodesWest[9] = new Subnode(new BlockPos(0, 1, 0), rotate(Direction.WEST, shapes[12]));
            subnodesWest[10] = new Subnode(new BlockPos(0, 1, 1), rotate(Direction.WEST, shapes[13]));
            subnodesWest[11] = new Subnode(new BlockPos(1, 1, -1), rotate(Direction.WEST, shapes[8]));
            subnodesWest[12] = new Subnode(new BlockPos(1, 1, 0), rotate(Direction.WEST, shapes[9]));
            subnodesWest[13] = new Subnode(new BlockPos(1, 1, 1), rotate(Direction.WEST, shapes[10]));

            return IMultiblockParentBlock.SubnodeWrapper.createDirectional(subnodesNorth, subnodesEast, subnodesSouth, subnodesWest);

        });




        public static IMultiblockParentBlock.SubnodeWrapper make(Supplier<IMultiblockParentBlock.SubnodeWrapper> sup) {
            return sup.get();
        }

        private static VoxelShape rotate(Direction to, VoxelShape shape) {
            VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

            int times = (to.get2DDataValue() - Direction.SOUTH.get2DDataValue() + 4) % 4;
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }

            return buffer[0];
        }
    }

}
