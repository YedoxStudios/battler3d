package io.yedox.imagine3d.world;

import io.yedox.imagine3d.block.Block;
import io.yedox.imagine3d.block.BlockType;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;

import static io.yedox.imagine3d.world.WorldGenerator.blockSize;
import static io.yedox.imagine3d.world.WorldGenerator.buildHeightLimit;

public class WorldBlockLoader {
    public Thread loadingThread;

    public WorldBlockLoader(PApplet applet) {
        int distance = 5;
        loadingThread = new Thread(() -> {
            for (int x = 0; x < blockSize; x++) {
                for (int y = 0; y < buildHeightLimit; y++) {
                    for (int z = 0; z < blockSize; z++) {
                        Block block = WorldGenerator.blocks[x][y][z];
                        Logger.logDebug(GUI.player.position.x + "" + GUI.player.position.z);
                        if ((x > GUI.player.position.x - distance && x < GUI.player.position.x + distance) && (z > GUI.player.position.z - distance && z < GUI.player.position.z + distance)) {
                            if (WorldGenerator.blocks[x - 1][y][z] != null ||  WorldGenerator.blocks[x - 1][y][z].getBlockType().equals(BlockType.PLATFORM))
                                WorldGenerator.blocks[x][y][z].renderLeft = false;
                            else
                                WorldGenerator.blocks[x][y][z].renderLeft = true;
                            if (x < blockSize - 1)
                                if (WorldGenerator.blocks[x + 1][y][z] != null ||  WorldGenerator.blocks[x + 1][y][z].getBlockType().equals(BlockType.PLATFORM))
                                    WorldGenerator.blocks[x][y][z].renderRight = false;
                                else
                                    WorldGenerator.blocks[x][y][z].renderRight = true;
                            if (z > 0)
                                if (WorldGenerator.blocks[x][y][z - 1] != null ||  WorldGenerator.blocks[x][y][z - 1].getBlockType().equals(BlockType.PLATFORM))
                                    WorldGenerator.blocks[x][y][z].renderBack = false;
                                else
                                    WorldGenerator.blocks[x][y][z].renderBack = true;
                            if (z < blockSize - 1)
                                try {
                                    if (WorldGenerator.blocks[x][y][z + 1] != null ||  WorldGenerator.blocks[x][y][z + 1].getBlockType().equals(BlockType.PLATFORM))
                                        WorldGenerator.blocks[x][y][z].renderFront = false;
                                } catch (Exception e) {
                                    WorldGenerator.blocks[x][y][z].renderFront = true;
                                }
                            if (y > 0)
                                if (WorldGenerator.blocks[x][y - 1][z] != null)
                                    if (WorldGenerator.blocks[x][y - 1][z].getBlockType().equals(BlockType.PLATFORM))
                                        WorldGenerator.blocks[x][y][z].renderTop = false;
                                    else
                                        WorldGenerator.blocks[x][y][z].renderTop = true;
                            if (y < buildHeightLimit - 1)
                                if (WorldGenerator.blocks[x][y + 1][z] != null || WorldGenerator.blocks[x][y + 1][z + 1].getBlockType().equals(BlockType.PLATFORM))
                                    WorldGenerator.blocks[x][y][z].renderBottom = false;
                                else
                                    WorldGenerator.blocks[x][y][z].renderBottom = true;
                        } else {
                            block.renderLeft = false;
                            block.renderRight = false;
                            block.renderBottom = false;
                            block.renderTop = false;
                            block.renderFront = false;
                            block.renderBack = false;

                        }
                    }
                }
            }
        });
    }

    public void start() {
        this.loadingThread.start();
    }
}
