package ballistix.prefab.screen;

import ballistix.Ballistix;
import electrodynamics.api.screen.ITexture;
import net.minecraft.resources.ResourceLocation;

public enum RadarTextures implements ITexture {
    FREQUENCY(18, 18, 0, 0, 18, 18, Ballistix.rl("textures/screen/component/radar/frequency.png"));

    private final int textureWidth;
    private final int textureHeight;
    private final int textureU;
    private final int textureV;
    private final int imageWidth;
    private final int imageHeight;
    private final ResourceLocation loc;

    private RadarTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureU = textureU;
        this.textureV = textureV;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.loc = loc;
    }

    public ResourceLocation getLocation() {
        return this.loc;
    }

    public int imageHeight() {
        return this.imageHeight;
    }

    public int imageWidth() {
        return this.imageWidth;
    }

    public int textureHeight() {
        return this.textureHeight;
    }

    public int textureU() {
        return this.textureU;
    }

    public int textureV() {
        return this.textureV;
    }

    public int textureWidth() {
        return this.textureWidth;
    }

}