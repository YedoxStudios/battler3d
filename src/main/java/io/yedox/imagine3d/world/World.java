package io.yedox.imagine3d.world;

import io.yedox.imagine3d.block.Block;
import io.yedox.imagine3d.utils.FormatConverter;
import io.yedox.imagine3d.block.SerializableBlock;

import java.io.Serializable;

public class World implements Serializable {
    private static final long serialVersionUID = 1L;

    // Size of the blocks
    public final int blockSize;

    // A two-dimensional array that contains the world voxels
    public final SerializableBlock[][] blockArray;

    // World metadata
    private final WorldMeta metadata;

    public World(Block[][] blocks, int blockSize, WorldMeta worldMeta){
        this.blockArray = FormatConverter.convertBlockArrayToSerializableArray(blocks, blockSize);
        this.blockSize = blockSize;
        this.metadata = worldMeta;
    }

    public WorldMeta getMetadata() {
        return this.metadata;
    }

    @Override
    public String toString() {
        return new StringBuffer("World Name: ").append(metadata.getWorldName()).toString();
    }
}
