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

package io.yedox.imagine3d.terrain;

import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.terrain.blocks.PlatformBlock;
import io.yedox.imagine3d.terrain.blocks.WaterBlock;
import io.yedox.imagine3d.util.BlockUtils;
import io.yedox.imagine3d.util.Logger;
import processing.core.PApplet;
import processing.core.PVector;

public class TerrainManager extends Thread {
    public final int blockSize;
    public final Block[][] blocks;
    private final PApplet pApplet;
    public PVector generationProgress;
    private boolean terrainGenerated;

    public TerrainManager(int blockSize, int yOffset, PApplet applet) {
        blocks = new Block[blockSize][blockSize];
        this.blockSize = blockSize;
        this.pApplet = applet;
        this.generationProgress = new PVector();
        this.terrainGenerated = false;
    }

    public void generateTerrain(PApplet applet) {
        float counter = 10;
//        for (int i = 0; i < blocksize; i++) {
//            for (int j = 0; j < blocksize; j++) {
//                float x = i * 5;
//                float y = 0;
//                float z = j * 5;
//                blocks[i][j] = new DirtBlock(applet, x, y, z, 5, 5, 5);
//                blocks[i][j].position.y = applet.random(0, 15);
//            }
//        }

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                blocks[i][j] = new WaterBlock(applet, i * 5, 0, j * 5);
                Logger.logDebug("Generating terrain: " + i + "x" + j + " (" + i + "%)");
                generationProgress.x = i;
            }
        }
        setTerrainGenerated(true);
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
