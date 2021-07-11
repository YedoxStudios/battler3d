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

package io.yedox.imagine3d.world;

import io.yedox.imagine3d.block.Block;
import io.yedox.imagine3d.block.BlockType;
import io.yedox.imagine3d.block.worldblock.PlatformBlock;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.BlockUtils;
import io.yedox.imagine3d.utils.FormatConverter;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;
import processing.core.PVector;

import java.time.LocalDateTime;

public class WorldGenerator extends Thread {
    public static int buildHeightLimit = 4;
    // Size of the block array
    public static int blockSize;
    private final PApplet pApplet;
    // Array which contains the blocks
    public static Block[][][] blocks;
    // Something idk
    public static PVector generationProgress;
    // Contains the world that is used
    // to load and save from files
    public World world;
    private boolean terrainGenerated;

    /**
     * Default constructor
     */
    public WorldGenerator(int blockSize, int buildHeightlimit, PApplet applet) {
        blocks = new Block[blockSize][blockSize][blockSize];
        this.blockSize = blockSize;
        this.pApplet = applet;
        this.generationProgress = new PVector();
        this.terrainGenerated = false;
        buildHeightLimit = buildHeightlimit;
    }

    /**
     * Initializes the voxel array
     */
    public void generateTerrain(PApplet applet) {

        for (int x = 0; x < blockSize; x++) {
            for (int y = 0; y < buildHeightLimit; y++) {
                for (int z = 0; z < blockSize; z++) {
                    int percentageGenerated = ((x + 1) * 100) / blockSize;
                    this.generationProgress.x = x;
                    this.blocks[x][y][z] = new PlatformBlock(applet, x * 5, y * 5, z * 5);
                    Logger.logDebug("Generating terrain: X:" + x + " Y:" + y + " Z:" + z + " (" + percentageGenerated + "%)");
                }
            }
        }

        this.setTerrainGenerated(true);

        for (int x = 0; x < blockSize; x++)
            for (int y = 0; y < buildHeightLimit; y++)
                for (int z = 0; z < blockSize; z++) {
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    blocks[x][y][z].renderTop = true;
                    blocks[x][y][z].renderBottom = true;
                    blocks[x][y][z].renderLeft = true;
                    blocks[x][y][z].renderRight = true;
                    blocks[x][y][z].renderFront = true;
                    blocks[x][y][z].renderBack = true;
                }

        for (int x = 0; x < blockSize; x++) {
            for (int y = 0; y < buildHeightLimit; y++) {
                for (int z = 0; z < blockSize; z++) {
                    try {
                        if (x > 0)
                            if (blocks[x - 1][y][z] != null || blocks[x - 1][y][z].getBlockType().equals(BlockType.PLATFORM))
                                this.blocks[x][y][z].renderLeft = false;
                            else
                                this.blocks[x][y][z].renderLeft = true;
                        if (x < blockSize - 1)
                            if (blocks[x + 1][y][z] != null || blocks[x + 1][y][z].getBlockType().equals(BlockType.PLATFORM))
                                this.blocks[x][y][z].renderRight = false;
                            else
                                this.blocks[x][y][z].renderRight = true;
                        if (z > 0)
                            if (blocks[x][y][z - 1] != null || blocks[x][y][z - 1].getBlockType().equals(BlockType.PLATFORM))
                                this.blocks[x][y][z].renderBack = false;
                            else
                                this.blocks[x][y][z].renderBack = true;
                        if (z < blockSize - 1)
                            try {
                                if (blocks[x][y][z + 1] != null || blocks[x][y][z + 1].getBlockType().equals(BlockType.PLATFORM))
                                    this.blocks[x][y][z].renderFront = false;
                            } catch (Exception e) {
                                this.blocks[x][y][z].renderFront = true;
                            }
                        if (y > 0)
                            if (blocks[x][y - 1][z] != null)
                                if (blocks[x][y - 1][z].getBlockType().equals(BlockType.PLATFORM))
                                    this.blocks[x][y][z].renderTop = false;
                                else
                                    this.blocks[x][y][z].renderTop = true;
                        if (y < buildHeightLimit - 1)
                            if (blocks[x][y + 1][z] != null || blocks[x][y + 1][z + 1].getBlockType().equals(BlockType.PLATFORM))
                                this.blocks[x][y][z].renderBottom = false;
                            else
                                this.blocks[x][y][z].renderBottom = true;

                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    Logger.logDebug("Generating rendermap: X:" + x + " Y:" + y + " Z:" + z);
                }
            }
        }


        Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);
//        GUI.worldBlockLoader.start();
        this.world = new World(blocks, blockSize, new WorldMeta("World", WorldGeneratorType.FLAT, LocalDateTime.now()));
    }

    /**
     * Loads a world from an object
     */
    public void loadWorld(World world, PApplet applet) {
        this.world = world;
        this.blocks = FormatConverter.convertSerializableArrayToBlockArray(world.blockArray, world.blockSize, pApplet);
        this.blockSize = world.blockSize;
    }

    /**
     * Returns the world object
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Renders all the blocks
     */
    public void renderTerrain() {
        for (int i = 0; i <= blockSize - 1; i++) {
            for (int j = 0; j <= buildHeightLimit - 1; j++) {
                for (int k = 0; k < blockSize - 1; k++) {
                    blocks[i][j][k].render();
                    blocks[i][j][k].update();
                }
            }
        }
    }

    /**
     * Returns terrainGenerated value
     */
    public boolean isTerrainGenerated() {
        return terrainGenerated;
    }

    /**
     * Sets the terrainGenerated value to the
     * specified boolean
     */
    public void setTerrainGenerated(boolean terrainGenerated) {
        this.terrainGenerated = terrainGenerated;
    }

    /**
     * Offsets voxel blocks' Y axis by
     * the specified min/max values
     */
    public void offsetBlocks(float min, float max) {
        for (int i = 0; i <= blockSize - 1; i++)
            for (int j = 0; j <= buildHeightLimit - 1; j++)
                for (int k = 0; k < blockSize - 1; k++)
                    blocks[i][j][k].position.y += blocks[i][j][k].applet.random(min, max);
    }

    /**
     * Returns the block at the specified
     * position
     */
    public Block getBlockAt(PVector pos) {
        PVector position = BlockUtils.getBlockCoords(pos);

//        for (int i = 0; i <= blockSize - 1; i++)
//            for (int j = 0; j <= buildHeightLimit - 1; j++)
//                for (int k = 0; k <= blockSize - 1; k++) {
//                    if (position.x == i && position.y == j && position.z == k) {
//                        return blocks[i][j][k];
//                    }
//                }

        return blocks[(int) pos.x][(int) pos.y][(int) pos.z];
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        generateTerrain(this.pApplet);
    }
}
