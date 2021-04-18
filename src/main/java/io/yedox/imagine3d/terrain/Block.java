package io.yedox.imagine3d.terrain;

import io.yedox.imagine3d.gui.GUI;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Block implements IBlock {
    /**
     * The ID of the current block
     */
    public static int BLOCK_ID = 0;

    /*
     * Block type
     */
    public static Material MATERIAL;

    /**
     * Contains an instance of the main applet
     */
    private final PApplet applet;

    /**
     * Texture of the block
     */
    public PImage blockTexture;

    /**
     * Block position
     */
    public PVector position;

    /**
     * Block size
     */
    public PVector dimensions;

    /**
     * Is the block destroyed?
     * Deprecated since alpha 1.3.2.
     * Use deleteBlock() method in the
     * TerrainManager class
     */
    @Deprecated
    private boolean destroyed = false;

    /**
     * Default constructor
     *
     * @param applet The main applet
     */
    public Block(PApplet applet, float x, float y, float z, float w, float h, float d) {
        this.position = new PVector(x, y, z);
        this.dimensions = new PVector(w, h, d);
        this.applet = applet;
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");

        MATERIAL = Material.AIR;
    }


    /**
     * Returns the block type
     *
     * @return BlockType
     */
    public static Material getMATERIAL() {
        return MATERIAL;
    }

    /**
     * Returns the BLOCKID of the current block
     *
     * @return int
     */
    public int getBlockid() {
        return BLOCK_ID;
    }

    public void update() {
            if (MATERIAL != Material.WATER || MATERIAL != Material.AIR) {
                float playerLeft = GUI.player.position.x - GUI.player.dimensions.x / 2;
                float playerRight = GUI.player.position.x + GUI.player.dimensions.x / 2;
                float playerTop = GUI.player.position.y - GUI.player.dimensions.y / 2;
                float playerBottom = GUI.player.position.y + GUI.player.dimensions.y / 2;
                float playerFront = GUI.player.position.z - GUI.player.dimensions.z / 2;
                float playerBack = GUI.player.position.z + GUI.player.dimensions.z / 2;

                float boxLeft = position.x - dimensions.x / 2;
                float boxRight = position.x + dimensions.x / 2;
                float boxTop = position.y - dimensions.y / 2;
                float boxBottom = position.y + dimensions.y / 2;
                float boxFront = position.z - dimensions.z / 2;
                float boxBack = position.z + dimensions.z / 2;

                float boxLeftOverlap = playerRight - boxLeft;
                float boxRightOverlap = boxRight - playerLeft;
                float boxTopOverlap = playerBottom - boxTop;
                float boxBottomOverlap = boxBottom - playerTop;
                float boxFrontOverlap = playerBack - boxFront;
                float boxBackOverlap = boxBack - playerFront;

                if (!destroyed && !GUI.player.flyMode) {
                    if (((playerLeft > boxLeft && playerLeft < boxRight || (playerRight > boxLeft && playerRight < boxRight)) && ((playerTop > boxTop && playerTop < boxBottom) || (playerBottom > boxTop && playerBottom < boxBottom)) && ((playerFront > boxFront && playerFront < boxBack) || (playerBack > boxFront && playerBack < boxBack)))) {
                        float xOverlap = PApplet.max(PApplet.min(boxLeftOverlap, boxRightOverlap), 0);
                        float yOverlap = PApplet.max(PApplet.min(boxTopOverlap, boxBottomOverlap), 0);
                        float zOverlap = PApplet.max(PApplet.min(boxFrontOverlap, boxBackOverlap), 0);

                        if (xOverlap < yOverlap && xOverlap < zOverlap) {
                            if (boxLeftOverlap < boxRightOverlap) {
                                GUI.player.position.x = boxLeft - GUI.player.dimensions.x / 2;
                            } else {
                                GUI.player.position.x = boxRight + GUI.player.dimensions.x / 2;
                            }
                        } else if (yOverlap < xOverlap && yOverlap < zOverlap) {
                            if (boxTopOverlap < boxBottomOverlap) {
                                GUI.player.position.y = boxTop - GUI.player.dimensions.y / 2;
                                GUI.player.velocity.y = 0;
                                GUI.player.grounded = true;
                            } else {
                                GUI.player.position.y = boxBottom + 1;
                            }
                        } else if (zOverlap < xOverlap && zOverlap < yOverlap) {
                            if (boxFrontOverlap < boxBackOverlap) {
                                GUI.player.position.z = boxFront - GUI.player.dimensions.x / 2;
                            } else {
                                GUI.player.position.z = boxBack + GUI.player.dimensions.x / 2;
                            }
                        }
                    }
                }
            }
    }

    public void draw() {
        if (MATERIAL != Material.AIR || MATERIAL != Material.WATER) {
            applet.pushMatrix();
            applet.noStroke();
            applet.translate(position.x, position.y, position.z);
            applet.scale(2.5f);
            drawCubeVert(blockTexture);
            applet.popMatrix();
        }
    }

    private void drawCubeVert(PImage texture) {
        applet.beginShape(PConstants.QUADS);
        applet.texture(texture);

        // +Z "front" face
        applet.vertex(-1, -1, 1, 0, 0);
        applet.vertex(1, -1, 1, 1, 0);
        applet.vertex(1, 1, 1, 1, 1);
        applet.vertex(-1, 1, 1, 0, 1);

        // -Z "back" face
        applet.vertex(1, -1, -1, 0, 0);
        applet.vertex(-1, -1, -1, 1, 0);
        applet.vertex(-1, 1, -1, 1, 1);
        applet.vertex(1, 1, -1, 0, 1);

        // +Y "bottom" face
        applet.vertex(-1, 1, 1, 0, 0);
        applet.vertex(1, 1, 1, 1, 0);
        applet.vertex(1, 1, -1, 1, 1);
        applet.vertex(-1, 1, -1, 0, 1);

        // -Y "top" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(1, -1, -1, 1, 0);
        applet.vertex(1, -1, 1, 1, 1);
        applet.vertex(-1, -1, 1, 0, 1);

        // +X "right" face
        applet.vertex(1, -1, 1, 0, 0);
        applet.vertex(1, -1, -1, 1, 0);
        applet.vertex(1, 1, -1, 1, 1);
        applet.vertex(1, 1, 1, 0, 1);

        // -X "left" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(-1, -1, 1, 1, 0);
        applet.vertex(-1, 1, 1, 1, 1);
        applet.vertex(-1, 1, -1, 0, 1);

        applet.endShape();
    }

    @Deprecated
    public void destroy() {
        this.destroyed = true;
        this.onBlockDestroy();
    }

    /**
     * Called when an entity walks on a block
     */
    @Override
    public void onEntityWalksOnBlock() {

    }

    /**
     * Called when the block is destroyed
     */
    @Override
    public void onBlockDestroy() {

    }
}
