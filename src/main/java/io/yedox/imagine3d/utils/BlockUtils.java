package io.yedox.imagine3d.utils;

import io.yedox.imagine3d.block.Block;
import processing.core.PVector;

public class BlockUtils {
    public static PVector getBlockCoords(PVector playerPosition) {
        return new PVector((Math.round(playerPosition.x / 5)), (Math.round(playerPosition.y / 5)), (Math.round(playerPosition.z / 5)));
    }

    public static PVector toBlockCoords(PVector standardCoords) {
        return new PVector((Math.round(standardCoords.x) * 5), 0, (Math.round(standardCoords.y) * 5));
    }

}
