package io.yedox.imagine3d.terrain.blocks;

import io.yedox.imagine3d.terrain.Block;
import io.yedox.imagine3d.terrain.BlockType;
import processing.core.PApplet;

public class PlatformBlock extends Block {
    /**
     * Default constructor
     *
     * @param applet  The main applet
     */
    public PlatformBlock(PApplet applet, float x, float y, float z, float w, float h, float d) {
        super(applet, x, y, z, w, h, d);
        this.BLOCK_TYPE = BlockType.PLATFORM;
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");
    }
}
