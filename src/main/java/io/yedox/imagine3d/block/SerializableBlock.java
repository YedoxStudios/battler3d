package io.yedox.imagine3d.block;

import processing.core.PVector;

import java.io.Serializable;

/**
 * Since the Block class contains fields that cannot be serialized,
 * we use another class for it. This class will contain the block
 * metadata so that we can use it in the World class to store the
 * blocks.
 */
public class SerializableBlock implements Serializable {
    // For serialization
    private static final long serialVersionUID = 2L;

    /**
     * Serializable BlockType
     */
    public String BLOCKTYPE;

    /**
     * Block id
     */
    private int blockId;

    /**
     * Block position
     */
    private PVector position;

    /**
     * Block size
     */
    private PVector dimensions;

    /**
     * Constructor
     */
    public SerializableBlock(PVector position, PVector dimensions, String blockType, int blockId) {
        this.position = position;
        this.dimensions = dimensions;
        this.BLOCKTYPE = blockType;
        this.blockId = blockId;
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getDimensions() {
        return dimensions;
    }

    public void setDimensions(PVector dimensions) {
        this.dimensions = dimensions;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}
