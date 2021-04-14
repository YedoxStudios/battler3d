package io.yedox.imagine3d.terrain.blocks;

import io.yedox.imagine3d.terrain.Block;
import io.yedox.imagine3d.terrain.BlockType;
import processing.core.PApplet;

public class WaterBlock extends Block {
    public WaterBlock(PApplet applet, float x, float y, float z) {
        super(applet, x, y, z, 5, 5, 5);
        this.BLOCK_TYPE = BlockType.WATER;
        this.blockTexture = applet.loadImage("textures/blocks/water.png");
    }
}
