package ballistix.client.render.tile;

import ballistix.client.ClientRegister;
import ballistix.common.tile.radar.TileFireControlRadar;
import com.mojang.blaze3d.vertex.PoseStack;
import electrodynamics.client.render.tile.AbstractTileRenderer;
import electrodynamics.prefab.utilities.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RenderFireControlRadar extends AbstractTileRenderer<TileFireControlRadar> {

    public RenderFireControlRadar(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileFireControlRadar tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        BakedModel radardish = getModel(ClientRegister.MODEL_FIRECONTROLRADARDISH);

        float partial = (float) (partialTicks * tileEntityIn.clientRotationSpeed);

        double yRot = tileEntityIn.clientRotation + partial;

        Direction facing = tileEntityIn.getFacing();

        if(facing == Direction.EAST || facing == Direction.WEST) {
            yRot += facing.toYRot();
        } else if(facing == Direction.SOUTH) {
            yRot -= 180.0;
        }

        matrixStackIn.translate(7.75 / 16.0, 11.0 / 16.0, 7.75 / 16.0);
        matrixStackIn.mulPose(MathUtils.rotQuaternionDeg(0, (float) -yRot, 0));
        // matrixStackIn.mulPose(new Quaternion(0,(float) ((tileEntityIn.savedTickRotation + partial)), 0, true));
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(tileEntityIn.getLevel(), radardish, tileEntityIn.getBlockState(), tileEntityIn.getBlockPos(), matrixStackIn, bufferIn.getBuffer(RenderType.solid()), false, tileEntityIn.getLevel().random, new Random().nextLong(), 0);
    }
}
