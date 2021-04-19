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

package io.yedox.imagine3d.terrain.blocks;

import io.yedox.imagine3d.terrain.Block;
import io.yedox.imagine3d.terrain.Material;
import processing.core.PApplet;

public class PlatformBlock extends Block {
    /**
     * Default constructor
     *
     * @param applet  The main applet
     */
    public PlatformBlock(PApplet applet, float x, float y, float z, float w, float h, float d) {
        super(applet, x, y, z, w, h, d);
        this.blockTexture = applet.loadImage("textures/blocks/platform.png");

        MATERIAL = Material.PLATFORM;
        BLOCKID = 99;
    }
}
