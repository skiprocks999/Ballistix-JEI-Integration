package ballistix.common.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import ballistix.References;
import ballistix.prefab.utils.BallistixTextUtils;
import ballistix.registers.BallistixCreativeTabs;
import ballistix.registers.BallistixDataComponentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME)
public class ItemTracker extends ItemElectric {

    public static HashMap<ServerLevel, HashSet<Integer>> validuuids = new HashMap<>();

    public ItemTracker() {
        super((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), BallistixCreativeTabs.MAIN, item -> ElectrodynamicsItems.ITEM_BATTERY.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        Component name = BallistixTextUtils.tooltip("tracker.none");
        if (stack.has(BallistixDataComponentTypes.TRACKER_TARGET) && stack.has(BallistixDataComponentTypes.TRACKER_ID)) {
            Entity entity = context.level().getEntity(stack.get(BallistixDataComponentTypes.TRACKER_ID));
            if (entity != null) {
                name = entity.getName();
            }
        }
        tooltip.add(BallistixTextUtils.tooltip("tracker.tracking", name).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level instanceof ServerLevel slevel) {
            if ((selected || entity instanceof Player player && player.getOffhandItem() == stack) && stack.has(BallistixDataComponentTypes.TRACKER_TARGET) && stack.has(BallistixDataComponentTypes.TRACKER_ID)) {
                int uuid = stack.get(BallistixDataComponentTypes.TRACKER_ID);
                if (validuuids.containsKey(level) && validuuids.get(level).contains(uuid)) {
                    Entity ent = slevel.getEntity(uuid);
                    if (ent != null) {
                        stack.set(BallistixDataComponentTypes.TRACKER_TARGET, new Target(ent.position().x, ent.position().y));
                    }
                } else {
                    stack.remove(BallistixDataComponentTypes.TRACKER_TARGET);
                    stack.remove(BallistixDataComponentTypes.TRACKER_ID);
                }
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (player != null && player.level() instanceof ServerLevel server && getJoulesStored(stack) >= 150) {
            Inventory inv = player.getInventory();
            inv.removeItem(stack);
            stack.set(BallistixDataComponentTypes.TRACKER_ID, entity.getId());
            HashSet<Integer> set = validuuids.getOrDefault(server, new HashSet<>());
            set.add(entity.getId());
            validuuids.put(server, set);
            if (hand == InteractionHand.MAIN_HAND) {
                inv.setItem(inv.selected, stack);
            } else {
                inv.offhand.set(0, stack);
            }
            extractPower(stack, 150, false);
        }
        return InteractionResult.PASS;
    }

    public static float getAngle(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int par) {
        Entity sourceEntity = entity != null ? entity : stack.getEntityRepresentation();
        if (sourceEntity == null || !stack.has(BallistixDataComponentTypes.TRACKER_TARGET)) {
            return 0F;
        }

        Target target = stack.get(BallistixDataComponentTypes.TRACKER_TARGET);

        double angleOfSource = 0.0D;
        if (entity instanceof Player player && player.isLocalPlayer()) {
            angleOfSource = entity.getYRot();
        } else if (sourceEntity instanceof ItemFrame itemFrameEntity) {
            Direction direction = itemFrameEntity.getDirection();
            int j = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
            angleOfSource = Mth.wrapDegrees(180 + direction.get2DDataValue() * 90L + itemFrameEntity.getRotation() * 45L + j);
        } else if (sourceEntity instanceof ItemEntity item) {
            angleOfSource = 180.0F - item.getSpin(0.5F) / ((float) Math.PI * 2F) * 360.0F;
        } else if (entity != null) {
            angleOfSource = entity.yBodyRot;
        }

        double rawAngleToTarget = Math.atan2(target.z() - sourceEntity.getZ(), target.x() - sourceEntity.getX()) / ((float) Math.PI * 2F);
        double adjustedAngleToTarget = 0.5D - (Mth.positiveModulo(angleOfSource / 360.0D, 1.0D) - 0.25D - rawAngleToTarget);

        return Mth.positiveModulo((float) adjustedAngleToTarget, 1.0F);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !oldStack.getItem().equals(newStack.getItem());
    }

    @SubscribeEvent
    public static void tick(ServerTickEvent.Pre event) {
        for (Entry<ServerLevel, HashSet<Integer>> en : validuuids.entrySet()) {
            Iterator<Integer> it = en.getValue().iterator();
            while (it.hasNext()) {
                int uuid = it.next();
                Entity ent = en.getKey().getEntity(uuid);
                if (ent == null || ent.isRemoved()) {
                    it.remove();
                }
            }
        }
    }

    public static record Target(double x, double z) {

        public static final Codec<Target> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.DOUBLE.fieldOf("x").forGetter(Target::x),
                Codec.DOUBLE.fieldOf("z").forGetter(Target::z)
        ).apply(instance, Target::new));

        public static final StreamCodec<ByteBuf, Target> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE, Target::x,
                ByteBufCodecs.DOUBLE, Target::z,
                Target::new
        );

    }

}
