package ballistix.prefab.screen;

import ballistix.client.screen.ScreenFireControlRadar;
import ballistix.common.tile.radar.TileFireControlRadar;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenComponentRadarGrid extends ScreenComponentGeneric {

    private static final Color RADAR_BLACK = new Color(0, 0, 0, 255);
    private static final Color RADAR_GREEN = new Color(36, 170, 90, 255);
    private static final Color RADAR_BLUE = new Color(13, 167, 255, 255);
    private static final Color RADAR_YELLOW = new Color(255, 246, 4, 255);
    private static final Color RADAR_RED = new Color(255, 0, 0, 255);

    public ScreenComponentRadarGrid(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

        int x = xLocation + guiWidth;
        int y = yLocation + guiHeight;

        //BG

        graphics.fill(x, y, x + width, y + height, RADAR_BLACK.color());

        //OUTLINE

        graphics.fill(x, y, x + 1, y + height, Color.TEXT_GRAY.color());

        graphics.fill(x + width - 1, y, x + width, y + height, Color.TEXT_GRAY.color());

        graphics.fill(x, y, x + width, y + 1, Color.TEXT_GRAY.color());

        graphics.fill(x, y + height - 1, x + width, y + height, Color.TEXT_GRAY.color());

        // GRID

        int gridWidth = 12;

        for(int i = 1; i < 10; i++) {

            graphics.fill(x + 1, y + gridWidth * i, x + this.width - 1, y + 1 + gridWidth * i, RADAR_GREEN.color());

        }

        for(int i = 1; i < 10; i++) {

            graphics.fill(x + + gridWidth * i, y + 1, x + 1 + gridWidth * i, y + height - 1, RADAR_GREEN.color());

        }

        TileFireControlRadar tile = ((ScreenFireControlRadar)gui).getMenu().getSafeHost();

        if(tile == null || tile.trackingPos.equals(TileFireControlRadar.OUT_OF_REACH) || !tile.running.get()) {
            return;
        }

        float center = (width - 2) / 2.0F + 1.0F;

        long time = System.currentTimeMillis() % 2500L;

        float ratio = time / 2500.0F;

        float min = ratio * center - 1.0F;

        float max = ratio * center - 1.0F;

        int xMin = (int) Math.floor(x + center - min);
        int xMax = (int) Math.ceil(x + center + max);
        int yMin = (int) Math.floor(y + center - min);
        int yMax = (int) Math.ceil(y + center + max);

        graphics.fill(xMin, yMin, xMin + 1, yMax, RADAR_BLUE.color());

        graphics.fill(xMax - 1, yMin, xMax, yMax, RADAR_BLUE.color());

        graphics.fill(xMin, yMin, xMax, yMin + 1, RADAR_BLUE.color());

        graphics.fill(xMin, yMax - 1, xMax, yMax, RADAR_BLUE.color());

        graphics.fill((int) Math.floor(x + center - 1), (int) Math.floor(y + center - 1), (int) Math.ceil(x + center + 1), (int) Math.ceil(y + center + 1), RADAR_YELLOW.color());

        float deltaX = (float) (tile.trackingPos.get().x - tile.getBlockPos().getX());

        float deltaZ = (float) (tile.trackingPos.get().z - tile.getBlockPos().getZ());

        //float mag = (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        //deltaX = deltaX / mag;

        //deltaZ = deltaZ / mag;

        float offsetX = deltaX % center;

        float offsetZ = deltaZ % center;

        graphics.fill((int) Math.floor(x + center + offsetX - 1), (int) Math.floor(y + center + offsetZ - 1), (int) Math.ceil(x + center + offsetX + 1), (int) Math.ceil(y + center + offsetZ + 1), RADAR_RED.color());

    }
}
