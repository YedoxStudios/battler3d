package io.yedox.imagine3d.terrain;

public interface IBlock {

    /**
     * Called when an entity walks on a block
     */
    void onEntityWalksOnBlock();

    /**
     * Called when the block is destroyed
     */
    void onBlockDestroy();
}
