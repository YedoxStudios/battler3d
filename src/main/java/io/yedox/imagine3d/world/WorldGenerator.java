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

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.world.blocks.Block;
import io.yedox.imagine3d.world.blocks.BlockConverter;
import io.yedox.imagine3d.world.blocks.worldblocks.PlatformBlock;
import processing.core.PApplet;
import processing.core.PVector;

import java.time.LocalDateTime;

public class WorldGenerator extends Thread {
    private final PApplet pApplet;
    public int blockSize;
    public Block[][] blocks;
    public PVector generationProgress;
    public World world;
    private boolean terrainGenerated;

    public WorldGenerator(int blockSize, int yOffset, PApplet applet) {
        blocks = new Block[blockSize][blockSize];
        this.blockSize = blockSize;
        this.pApplet = applet;
        this.generationProgress = new PVector();
        this.terrainGenerated = false;
    }

    public void generateTerrain(PApplet applet) {
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                blocks[i][j] = new PlatformBlock(applet, i * 5, 0, j * 5);
                // Offsets a block by 0.4 px
                // blocks[i][j].position.y += applet.random(0, 0.4f);
                Logger.logDebug("Generating terrain: " + i + "x" + j + " (" + i + "%)");
                generationProgress.x = i;
            }
        }
        setTerrainGenerated(true);
        Game.setCurrentScreen(Game.Screen.MAIN_GAME_SCREEN);

        this.world = new World(blocks, blockSize, new WorldMeta("World", Game.releaseVersion, WorldGeneratorType.FLAT, LocalDateTime.now()));
    }

    /**
     * Loads a world from an object
     */
    public void loadWorld(World world, PApplet applet) {
        this.world = world;
        this.blocks = BlockConverter.convertSerializableArrayToBlockArray(world.blockArray, world.blockSize, pApplet);
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
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                blocks[i][j].draw();
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
