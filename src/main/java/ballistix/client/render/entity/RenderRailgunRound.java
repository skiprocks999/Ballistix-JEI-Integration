package ballistix.client.render.entity;

import ballistix.common.entity.EntityRailgunRound;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderRailgunRound extends EntityRenderer<EntityRailgunRound> {

    public RenderRailgunRound(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityRailgunRound p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRailgunRound entity) {
        return null;
    }
}
