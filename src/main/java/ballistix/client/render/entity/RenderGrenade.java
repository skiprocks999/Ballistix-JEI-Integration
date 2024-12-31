package ballistix.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import ballistix.common.entity.EntityGrenade;
import ballistix.common.item.ItemGrenade.SubtypeGrenade;
import ballistix.registers.BallistixItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

public class RenderGrenade extends EntityRenderer<EntityGrenade> {
    private ItemEntity itemEntity;
    private ItemEntityRenderer itemRenderer;

    public RenderGrenade(Context renderManagerIn) {
        super(renderManagerIn);
        itemRenderer = new ItemEntityRenderer(renderManagerIn);
        shadowRadius = 0.15F;
        shadowStrength = 0.75F;
    }

    @Override
    public void render(EntityGrenade entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        SubtypeGrenade subtype = entityIn.getExplosiveType();
        if (subtype != null) {
            matrixStackIn.pushPose();
            if (itemEntity == null) {
                itemEntity = new ItemEntity(EntityType.ITEM, entityIn.level());
            }
            itemEntity.setPos(entityIn.getX(), entityIn.getY(), entityIn.getZ());
            itemEntity.setItem(new ItemStack(BallistixItems.ITEMS_GRENADE.getValue(subtype)));
            matrixStackIn.translate(0, -0.5 / 16.0, 0);
            itemRenderer.render(itemEntity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGrenade entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
