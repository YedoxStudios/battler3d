/**
 * MIT License
 *
 * Copyright (c) 2021 Yedox Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.yedox.imagine3d.terrain;

import io.yedox.imagine3d.terrain.blocks.PlatformBlock;
import io.yedox.imagine3d.util.Logger;
import processing.core.PApplet;
import processing.core.PVector;

public class WorldRenderer extends Thread {
    public PVector progress;
    private boolean terrainGenerated = false;
    public final int blocksize;
    public final Block[][] blocks;
    private PApplet pApplet;

    public WorldRenderer(int blockSize, int yOffset, PApplet applet) {
        blocks = new Block[blockSize][blockSize];
        this.blocksize = blockSize;
        this.pApplet = applet;
        this.progress = new PVector();
    }

    public void generateTerrain(PApplet applet) {
//        float counter = 10;
//        for (int i = 0; i < blocksize; i++) {
//            for (int j = 0; j < blocksize; j++) {
//                float x = i * 5;
//                float y = 0;
//                float z = j * 5;
//                blocks[i][j] = new DirtBlock(applet, x, y, z, 5, 5, 5);
//                blocks[i][j].position.y = applet.random(0, 15);
//            }
//        }

        for (int i = 0; i < blocksize; i++) {
            for (int j = 0; j < blocksize; j++) {
                blocks[i][j] = new PlatformBlock(applet, i * 5, 0, j * 5, 5, 5, 5);
                Logger.logDebug("Generating terrain: " + i + "x" + j + " [" + i + "%]");
                progress.x = i;
            }
        }
        setTerrainGenerated(true);
    }

    public void renderTerrain() {
        for (int i = 0; i < blocksize; i++) {
            for (int j = 0; j < blocksize; j++) {
                blocks[i][j].draw();
                blocks[i][j].update();
            }
        }
    }

    public boolean isTerrainGenerated() {
        return terrainGenerated;
    }

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
