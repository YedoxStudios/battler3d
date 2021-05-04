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
import io.yedox.imagine3d.block.worldblock.PlatformBlock;
import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.utils.BlockUtils;
import io.yedox.imagine3d.utils.FormatConverter;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;
import processing.core.PVector;

import java.time.LocalDateTime;

public class WorldGenerator extends Thread {
    // Size of the block array
    public int blockSize;

    // Array which contains the blocks
    public Block[][] blocks;

    // Something idk
    public PVector generationProgress;

    // Contains the world that is used
    // to load and save from files
    public World world;

    private final PApplet pApplet;
    private boolean terrainGenerated;

    /**
     * Default constructor
     */
    public WorldGenerator(int blockSize, PApplet applet) {
        blocks = new Block[blockSize][blockSize];
        this.blockSize = blockSize;
        this.pApplet = applet;
        this.generationProgress = new PVector();
        this.terrainGenerated = false;
    }

    /**
     * Initializes the voxel array
     */
    public void generateTerrain(PApplet applet) {
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                int percentageGenerated = ((i + 1) * 100) / blockSize;
                this.generationProgress.x = i;
                this.blocks[i][j] = new PlatformBlock(applet, i * 5, 0, j * 5);
                Logger.logDebug("Generating terrain: " + i + "x" + j + " (" + percentageGenerated  + "%)");
            }
        }
        this.setTerrainGenerated(true);
        Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);

        this.world = new World(blocks, blockSize, new WorldMeta("World", Game.releaseVersion, WorldGeneratorType.FLAT, LocalDateTime.now()));
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
            for (int j = 0; j <= blockSize - 1; j++) {
                blocks[i][j].render();
                blocks[i][j].update();
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
            for (int j = 0; j <= blockSize- 1; j++)
                blocks[i][j].position.y += blocks[i][j].applet.random(min, max);
    }

    /**
     * Returns the block at the specified
     * position
     */
    public Block getBlockAt(PVector position) {
        PVector blockPosition = BlockUtils.toBlockCoords(position);

        for (int i = 0; i <= blockSize - 1; i++)
            for (int j = 0; j <= blockSize - 1; j++)
                if (blocks[i][j].position == position)
                    return blocks[i][j];

        return null;
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
