package io.yedox.imagine3d.utils;

import io.yedox.imagine3d.block.Block;
import io.yedox.imagine3d.block.SerializableBlock;
import io.yedox.imagine3d.world.WorldGenerator;
import processing.core.PApplet;

/**
 * Allows you to convert Objects in a serializable
 * format to their normal format
 */
public class FormatConverter {
    public static Block convertSerializableToBlock(SerializableBlock block, PApplet applet) {
        return new Block(applet, block.getPosition().x, block.getPosition().y, block.getPosition().z, block.getDimensions().x, block.getDimensions().y, block.getDimensions().z, block.BLOCKTYPE, block.getBlockId());
    }

    public static SerializableBlock convertBlockToSerializable(Block block) {
        return new SerializableBlock(block.position, block.dimensions, block.BLOCKTYPE, block.getBlockid());
    }

    public static SerializableBlock[][][] convertBlockArrayToSerializableArray(Block[][][] blocks, int blockSize) {
        SerializableBlock[][][] tempBlocks = new SerializableBlock[blockSize][WorldGenerator.buildHeightLimit][blockSize];

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < WorldGenerator.buildHeightLimit; j++) {
                for (int k = 0; k < blockSize; k++) {
                    tempBlocks[i][j][k] = convertBlockToSerializable(blocks[i][j][k]);
                }
            }
        }

        return tempBlocks;
    }

    /**
     * Converts Serializable block array to standard
     * block array
     */
    public static Block[][][] convertSerializableArrayToBlockArray(SerializableBlock[][][] blocks, int blockSize, PApplet applet) {
        Block[][][] tempBlocks = new Block[blockSize][WorldGenerator.buildHeightLimit][blockSize];

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < WorldGenerator.buildHeightLimit; j++) {
                for (int k = 0; k < blockSize; k++) {
                    tempBlocks[i][j][k] = convertSerializableToBlock(blocks[i][j][k], applet);
                }
            }
        }

        return tempBlocks;
    }
}
