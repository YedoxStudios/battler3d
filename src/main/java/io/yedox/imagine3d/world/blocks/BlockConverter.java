package io.yedox.imagine3d.world.blocks;

import processing.core.PApplet;

/**
 * Allows you to convert serializable blocks
 * to standard blocks
 */
public class BlockConverter {
    public static Block convertSerializableToBlock(SerializableBlock block, PApplet applet) {
        return new Block(applet, block.getPosition().x, block.getPosition().y, block.getPosition().z, block.getDimensions().x, block.getDimensions().y, block.getDimensions().z, block.BLOCKTYPE, block.getBlockId());
    }

    public static SerializableBlock convertBlockToSerializable(Block block) {
        return new SerializableBlock(block.position, block.dimensions, block.BLOCKTYPE, block.getBlockid());
    }

    public static SerializableBlock[][] convertBlockArrayToSerializableArray(Block[][] blocks, int blockSize) {
        SerializableBlock[][] tempBlocks = new SerializableBlock[blockSize][blockSize];

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                tempBlocks[i][j] = convertBlockToSerializable(blocks[i][j]);
            }
        }

        return tempBlocks;
    }

    public static Block[][] convertSerializableArrayToBlockArray(SerializableBlock[][] blocks, int blockSize, PApplet applet) {
        Block[][] tempBlocks = new Block[blockSize][blockSize];

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                tempBlocks[i][j] = convertSerializableToBlock(blocks[i][j], applet);
            }
        }

        return tempBlocks;
    }
}
