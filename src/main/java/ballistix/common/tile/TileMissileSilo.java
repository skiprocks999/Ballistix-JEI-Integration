package ballistix.common.tile;

import ballistix.Ballistix;
import ballistix.common.block.subtype.SubtypeBallistixMachine;
import ballistix.registers.BallistixDataComponentTypes;
import ballistix.registers.BallistixSounds;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;

import ballistix.References;
import ballistix.api.silo.SiloRegistry;
import ballistix.common.block.BlockExplosive;
import ballistix.common.entity.EntityMissile;
import ballistix.common.inventory.container.ContainerMissileSilo;
import ballistix.common.item.ItemMissile;
import ballistix.common.settings.Constants;
import ballistix.registers.BallistixDataComponentTypes;
import ballistix.registers.BallistixItems;
import ballistix.registers.BallistixTiles;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock.SubnodeWrapper;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.common.blockitem.types.BlockItemDescriptable;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.items.IItemHandler;

public class TileMissileSilo extends GenericTile implements IMultiblockParentTile {

    public static final int MISSILE_SLOT = 0;
    public static final int EXPLOSIVE_SLOT = 1;

    public static final int COOLDOWN = 100;

    public Property<Integer> range = property(new Property<>(PropertyTypes.INTEGER, "range", 0));
    public Property<Boolean> hasExplosive = property(new Property<>(PropertyTypes.BOOLEAN, "hasexplosive", false));
    public Property<Integer> frequency = property(new Property<>(PropertyTypes.INTEGER, "frequency", 0).onChange((prop, prevFreq) -> {

        if (level == null || level.isClientSide) {
            return;
        }

        int newFreq = prop.get();

        SiloRegistry.unregisterSilo(prevFreq, this);
        SiloRegistry.registerSilo(newFreq, this);

    }));
    public Property<BlockPos> target = property(new Property<>(PropertyTypes.BLOCK_POS, "target", BlockPos.ZERO));
    public Property<Integer> hasRedstoneSignal = property(new Property<>(PropertyTypes.INTEGER, "hasredstonesignal", 0x00000));

    private int cooldown = 100;
    public boolean shouldLaunch = false;

    public TileMissileSilo(BlockPos pos, BlockState state) {
        super(BallistixTiles.TILE_MISSILESILO.get(), pos, state);

        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).voltage(120).maxJoules(Constants.MISSILESILO_USAGE * 20).setInputDirections(BlockEntityUtils.MachineDirection.values()));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(3)).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.values()).setDirectionsBySlot(1, BlockEntityUtils.MachineDirection.values()).valid(this::isItemValidForSlot));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentContainerProvider("container.missilesilo", this).createMenu((id, player) -> new ContainerMissileSilo(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));

    }

    protected void tickServer(ComponentTickable tickable) {

        if (target.get() == null) {
            target.set(getBlockPos());
        }

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        if (cooldown > 0 || electro.getJoulesStored() < Constants.MISSILESILO_USAGE) {
            cooldown--;
            return;
        }

        if (range.get() == 0 || !hasExplosive.get() || (hasRedstoneSignal.get() == 0 && !shouldLaunch)) {
            return;
        }

        shouldLaunch = false;

        double dist = calculateDistance(worldPosition, target.get());

        if (range.get() == 0 || (range.get() > 0 && range.get() < dist)) {
            return;
        }

        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ItemStack explosive = inv.getItem(EXPLOSIVE_SLOT);
        ItemStack mis = inv.getItem(MISSILE_SLOT);

        EntityMissile missile;

        int ordinal = ((ItemMissile) mis.getItem()).missile.ordinal();

        if (ordinal == 0) {

            missile = new EntityMissile.EntityMissileCloseRange(level);

        } else if (ordinal == 1) {

            missile = new EntityMissile.EntityMissileMediumRange(level);

        } else {

            missile = new EntityMissile.EntityMissileLongRange(level);

        }

        missile.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 20.5, getBlockPos().getZ() + 0.5);
        missile.missileType = ordinal;
        missile.target = target.get();
        missile.blastOrdinal = ((BlockExplosive) ((BlockItemDescriptable) explosive.getItem()).getBlock()).explosive.ordinal();
        missile.startX = (float) missile.getX();
        missile.startZ = (float) missile.getZ();
        missile.speed = 0;
        missile.frequency = frequency.get();
        missile.setDeltaMovement(new Vec3(0, 1, 0));

        electro.joules(electro.getJoulesStored() - Constants.MISSILESILO_USAGE);

        inv.removeItem(MISSILE_SLOT, 1);
        inv.removeItem(EXPLOSIVE_SLOT, 1);

        level.addFreshEntity(missile);

        level.playSound(null, getBlockPos(), BallistixSounds.SOUND_MISSILE_SILO.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

        cooldown = COOLDOWN;

    }

    protected boolean isItemValidForSlot(int index, ItemStack stack, ComponentInventory inv) {
        Item item = stack.getItem();

        if (index == 0) {
            return item instanceof ItemMissile;
        } else if (index == 1) {
            return item instanceof BlockItemDescriptable des && des.getBlock() instanceof BlockExplosive;
        } else if (index == 2) {
            return stack.is(BallistixItems.ITEM_RADARGUN) || stack.is(BallistixItems.ITEM_LASERDESIGNATOR);
        }
        return false;
    }

    @Override
    public void onBlockDestroyed() {
        if (level.isClientSide) {
            return;
        }
        SiloRegistry.unregisterSilo(frequency.get(), this);

        ChunkPos chunkPos = level.getChunk(worldPosition).getPos();

        ChunkloaderManager.TICKET_CONTROLLER.forceChunk((ServerLevel) level, worldPosition, chunkPos.x, chunkPos.z, false, true);

    }

    @Override
    public void onPlace(BlockState oldState, boolean isMoving) {
        super.onPlace(oldState, isMoving);
        if (level.isClientSide) {
            return;
        }
        ChunkPos chunkPos = level.getChunk(worldPosition).getPos();

        ChunkloaderManager.TICKET_CONTROLLER.forceChunk((ServerLevel) level, worldPosition, chunkPos.x, chunkPos.z, true, true);
    }

    @Override
    public void onNeightborChanged(BlockPos neighbor, boolean blockStateChange) {
        if (level.isClientSide) {
            return;
        }
        if (level.hasNeighborSignal(getBlockPos())) {
            setRedstoneSignal(0);
        } else {
            clearRedstoneSignal(0);
        }

    }

    @Override
    public void onSubnodeNeighborChange(TileMultiSubnode subnode, BlockPos subnodeChangingNeighbor, boolean blockStateChange) {
        if (level.isClientSide || subnodeChangingNeighbor.equals(getBlockPos())) {
            return;
        }
        BlockState state = level.getBlockState(subnodeChangingNeighbor);
        if (state.isAir() || state.is(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE)) {
            return;
        }
        if (level.hasNeighborSignal(subnode.getBlockPos())) {
            setRedstoneSignal(subnode.nodeIndex.getIndex() + 1);
        } else {
            clearRedstoneSignal(subnode.nodeIndex.getIndex() + 1);
        }
    }

    private void clearRedstoneSignal(int index) {
        int redstone = hasRedstoneSignal.get() & ~(1 << index);
        hasRedstoneSignal.set(redstone);

    }

    private void setRedstoneSignal(int index) {
        int redstone = hasRedstoneSignal.getIndex() | (1 << index);
        hasRedstoneSignal.set(redstone);
    }

    @Override
    public IMultiblockParentBlock.SubnodeWrapper getSubNodes() {

        return SubtypeBallistixMachine.Subnodes.MISSILE_SILO;
    }

    @Override
    public void onInventoryChange(ComponentInventory inv, int index) {

        handleMissile(inv, index);

        handleExplosive(inv, index);

        handleSync(inv, index);

    }

    private void handleMissile(ComponentInventory inv, int index) {
        if (index == 0 || index == -1) {

            ItemStack missile = inv.getItem(0);

            if (missile.isEmpty()) {
                range.set(0);
                return;
            }

            if (missile.getItem() instanceof ItemMissile item) {

                switch (item.missile) {

                    case closerange:
                        range.set(Constants.CLOSERANGE_MISSILE_RANGE);
                        break;
                    case mediumrange:
                        range.set(Constants.MEDIUMRANGE_MISSILE_RANGE);
                        break;
                    case longrange:
                        range.set(Constants.LONGRANGE_MISSILE_RANGE);
                        break;
                    default:
                        range.set(0);
                        break;
                }

            } else {
                range.set(0);
            }

        }
    }

    private void handleExplosive(ComponentInventory inv, int index) {
        if (index == 1 || index == -1) {

            ItemStack explosive = inv.getItem(1);

            if (!explosive.isEmpty() && explosive.getItem() instanceof BlockItemDescriptable blockItem && blockItem.getBlock() instanceof BlockExplosive) {
                hasExplosive.set(true);
            } else {
                hasExplosive.set(false);
            }

        }
    }

    private void handleSync(ComponentInventory inv, int index) {
        if (index == 2 || index == -1) {

            ItemStack sync = inv.getItem(2);

            if (sync.isEmpty()) {
                return;
            }

            if (sync.is(BallistixItems.ITEM_LASERDESIGNATOR)) {

                sync.set(BallistixDataComponentTypes.BOUND_FREQUENCY, frequency.get());

            } else if (sync.is(BallistixItems.ITEM_RADARGUN)) {

                if (sync.has(ElectrodynamicsDataComponentTypes.BLOCK_POS)) {
                    target.set(sync.get(ElectrodynamicsDataComponentTypes.BLOCK_POS));
                }

            }

        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            SiloRegistry.registerSilo(frequency.get(), this);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("silocooldown", cooldown);
        compound.putBoolean("shouldlaunch", shouldLaunch);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        cooldown = compound.getInt("silocooldown");
        shouldLaunch = compound.getBoolean("shouldlaunch");
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.getItem() == BallistixItems.ITEM_RADARGUN.get() || handStack.getItem() == BallistixItems.ITEM_LASERDESIGNATOR.get()) {
            return ItemInteractionResult.FAIL;
        }
        return super.useWithItem(used, player, hand, hit);
    }

    @Override
    public void onSubnodeDestroyed(TileMultiSubnode arg0) {
        level.destroyBlock(worldPosition, true);
    }

    @Override
    public Direction getFacingDirection() {
        return getFacing();
    }

    @Override
    public ItemInteractionResult onSubnodeUseWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
        return useWithItem(used, player, hand, hit);
    }

    @Override
    public InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
        return useWithoutItem(player, hit);
    }

    @Override
    public @Nullable IItemHandler getSubnodeItemHandlerCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return getItemHandlerCapability(side);
    }

    @Override
    public @Nullable ICapabilityElectrodynamic getSubnodeElectrodynamicCapability(TileMultiSubnode subnode, @Nullable Direction side) {
        return getElectrodynamicCapability(side);
    }

    public static double calculateDistance(BlockPos fromPos, BlockPos toPos) {
        double deltaX = fromPos.getX() - toPos.getX();
        double deltaY = fromPos.getY() - toPos.getY();
        double deltaZ = fromPos.getZ() - toPos.getZ();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    @EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
    private static final class ChunkloaderManager {

        private static final TicketController TICKET_CONTROLLER = new TicketController(Ballistix.rl("chunkloadercontroller"));

        @SubscribeEvent
        public static void register(RegisterTicketControllersEvent event) {
            event.register(TICKET_CONTROLLER);
        }


    }

}
