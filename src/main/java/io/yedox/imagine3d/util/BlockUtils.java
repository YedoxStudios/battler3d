package io.yedox.imagine3d.util;

import processing.core.PVector;

public class BlockUtils {
    public static PVector getBlockCoords(PVector playerCoords) {
        return new PVector(5*(Math.round(playerCoords.x/5)), 5*(Math.round(playerCoords.y/5)));
    }
}
