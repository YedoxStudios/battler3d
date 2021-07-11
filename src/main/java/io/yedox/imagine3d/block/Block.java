/**
 * MIT License
 * <p>
 * Copyright (c) 2021 Yedox Studios
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.yedox.imagine3d.block;

import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Particle;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.io.Serializable;

public class Block implements IBlock, Serializable {

    // For serialization
    private static final long serialVersionUID = 2L;

    /**
     * The ID of the current block
     */
    public static int BLOCKID;

    /*
     * Block type
     */
    public static String BLOCKTYPE;

    /**
     * Contains an instance of the main applet
     */
    public final PApplet applet;

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
     * Render these sides
     */
    public boolean renderLeft = false;
    public boolean renderRight = false;
    public boolean renderBottom = false;
    public boolean renderTop = false;
    public boolean renderFront = false;
    public boolean renderBack = false;
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
     */
    public Block(PApplet applet, float x, float y, float z, float w, float h, float d) {
        this.position = new PVector(x, y, z);
        this.dimensions = new PVector(w, h, d);
        this.applet = applet;
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");
        BLOCKTYPE = BlockType.PLATFORM;
        BLOCKID = 0;
    }

    /**
     * Constructor with BlockType
     */
    public Block(PApplet applet, float x, float y, float z, float w, float h, float d, String blockType) {
        this.position = new PVector(x, y, z);
        this.dimensions = new PVector(w, h, d);
        this.applet = applet;
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");
        BLOCKTYPE = blockType;
        BLOCKID = 0;
    }

    /**
     * Constructor with BlockType and BlockID
     */
    public Block(PApplet applet, float x, float y, float z, float w, float h, float d, String blockType, int blockId) {
        this.position = new PVector(x, y, z);
        this.dimensions = new PVector(w, h, d);
        this.applet = applet;
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");
        BLOCKTYPE = blockType;
        BLOCKID = blockId;
        getBlockid();
    }

    /**
     * Returns the block type
     *
     * @return String
     */
    public static String getMaterial() {
        return BLOCKTYPE;
    }

    public void setRenderSides(int side, boolean render) {
        switch (side) {
            case 0:
                renderLeft = render;
                break;
            case 1:
                renderRight = render;
                break;
            case 2:
                renderBottom = render;
                break;
            case 3:
                renderTop = render;
                break;
            case 4:
                renderFront = render;
                break;
            case 5:
                renderBack = render;
                break;
            default:
                break;
        }
    }

    /**
     * Returns the Block ID of the current block
     *
     * @return int
     */
    public int getBlockid() {
        return BLOCKID;
    }

    /**
     * Updates the block
     */
    public void update() {
        if (!BLOCKTYPE.equals(BlockType.WATER) || !BLOCKTYPE.equals(BlockType.AIR)) {
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

            if (!destroyed && !GUI.player.observerMode) {
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

            for (int i = 0; i < GUI.particleSystem.particles.size(); i++) {
                Particle particle = GUI.particleSystem.particles.get(i);

                float playerleft = particle.position.x - particle.dimensions.x / 2;
                float playerright = particle.position.x + particle.dimensions.x / 2;
                float playertop = particle.position.y - particle.dimensions.y / 2;
                float playerbottom = particle.position.y + particle.dimensions.y / 2;
                float playerfront = particle.position.z - particle.dimensions.z / 2;
                float playerback = particle.position.z + particle.dimensions.z / 2;

                float boxleft = position.x - dimensions.x / 2;
                float boxright = position.x + dimensions.x / 2;
                float boxtop = position.y - dimensions.y / 2;
                float boxbottom = position.y + dimensions.y / 2;
                float boxfront = position.z - dimensions.z / 2;
                float boxback = position.z + dimensions.z / 2;

                float boxleftoverlap = playerright - boxleft;
                float boxrightoverlap = boxright - playerleft;
                float boxtopoverlap = playerbottom - boxtop;
                float boxbottomoverlap = boxbottom - playertop;
                float boxfrontoverlap = playerback - boxfront;
                float boxbackoverlap = boxback - playerfront;

                if (!destroyed) {
                    if (((playerleft > boxleft && playerleft < boxright || (playerright > boxleft && playerright < boxright)) && ((playertop > boxtop && playertop < boxbottom) || (playerbottom > boxtop && playerbottom < boxbottom)) && ((playerfront > boxfront && playerfront < boxback) || (playerback > boxfront && playerback < boxback)))) {
                        float xOverlap = PApplet.max(PApplet.min(boxleftoverlap, boxrightoverlap), 0);
                        float yOverlap = PApplet.max(PApplet.min(boxtopoverlap, boxbottomoverlap), 0);
                        float zOverlap = PApplet.max(PApplet.min(boxfrontoverlap, boxbackoverlap), 0);

                        if (xOverlap < yOverlap && xOverlap < zOverlap) {
                            if (boxleftoverlap < boxrightoverlap) {
                                particle.position.x = boxleft;
                            } else {
                                particle.position.x = boxright;
                            }
                        } else if (yOverlap < xOverlap && yOverlap < zOverlap) {
                            if (boxtopoverlap < boxbottomoverlap) {
                                particle.position.y = boxtop;
                                particle.velocity = new PVector(0,0,0);
                            } else {
                                particle.position.y = boxbottom + 1;
                                particle.velocity = new PVector(0,0,0);
                            }
                        } else if (zOverlap < xOverlap && zOverlap < yOverlap) {
                            if (boxfrontoverlap < boxbackoverlap) {
                                particle.position.z = boxfront;
                            } else {
                                particle.position.z = boxback;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders the block into the specified applet
     */
    public void render() {
        if (BLOCKTYPE != BlockType.AIR) {
            applet.pushMatrix();
            applet.noStroke();
            applet.translate(position.x, position.y, position.z);
            applet.scale(2.5f);
            this.cubeVertex(blockTexture);
            applet.popMatrix();
        }
    }

    /**
     * Private function to draw
     * box shape
     */
    private void cubeVertex(PImage texture) {
        applet.beginShape(PConstants.QUADS);
        applet.texture(texture);

        // +Z "front" face
        if (renderFront) {
            applet.vertex(-1, -1, 1, 0, 0);
            applet.vertex(1, -1, 1, 1, 0);
            applet.vertex(1, 1, 1, 1, 1);
            applet.vertex(-1, 1, 1, 0, 1);
        }

        if (renderBack) {
            // -Z "back" face
            applet.vertex(1, -1, -1, 0, 0);
            applet.vertex(-1, -1, -1, 1, 0);
            applet.vertex(-1, 1, -1, 1, 1);
            applet.vertex(1, 1, -1, 0, 1);
        }

        if (renderBottom) {
            // +Y "bottom" face
            applet.vertex(-1, 1, 1, 0, 0);
            applet.vertex(1, 1, 1, 1, 0);
            applet.vertex(1, 1, -1, 1, 1);
            applet.vertex(-1, 1, -1, 0, 1);
        }

        if (renderTop) {
            // -Y "top" face
            applet.vertex(-1, -1, -1, 0, 0);
            applet.vertex(1, -1, -1, 1, 0);
            applet.vertex(1, -1, 1, 1, 1);
            applet.vertex(-1, -1, 1, 0, 1);
        }

        if (renderRight) {
            // +X "right" face
            applet.vertex(1, -1, 1, 0, 0);
            applet.vertex(1, -1, -1, 1, 0);
            applet.vertex(1, 1, -1, 1, 1);
            applet.vertex(1, 1, 1, 0, 1);
        }

        if (renderLeft) {
            // -X "left" face
            applet.vertex(-1, -1, -1, 0, 0);
            applet.vertex(-1, -1, 1, 1, 0);
            applet.vertex(-1, 1, 1, 1, 1);
            applet.vertex(-1, 1, -1, 0, 1);
        }

        applet.endShape();
    }

    public void destroy() {
        this.BLOCKTYPE = BlockType.AIR;
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

    public String getBlockType() {
        return BlockType.PLATFORM;
    }
}
