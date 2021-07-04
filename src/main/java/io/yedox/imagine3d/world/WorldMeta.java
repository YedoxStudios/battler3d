package io.yedox.imagine3d.world;

import io.yedox.imagine3d.core.Game;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WorldMeta implements Serializable {
    // For serialization
    private static final long serialVersionUID = 2L;

    /**
     * The version that the world is
     * saved in. Might be useful for
     * converting older worlds to a
     * newer world format.
     */
    private final long WORLD_FORMAT;

    /**
     * Name of the world
     */
    private String worldName;

    /**
     * Type of the world
     */
    private WorldGeneratorType worldGeneratorType;

    /**
     * The time when world was saved
     */
    private LocalDateTime savedDateTime;

    public WorldMeta(String worldName, WorldGeneratorType generatorType, LocalDateTime localDateTime) {
        this.worldName = worldName;
        this.worldGeneratorType = generatorType;
        this.savedDateTime = localDateTime;
        this.WORLD_FORMAT = getWorldFormat();
    }

    private static long getWorldFormat() {
        if(Game.isBeta()) {
            return WorldFormat.BETA;
        } else {
            return WorldFormat.RELEASE;
        }
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public WorldGeneratorType getWorldGeneratorType() {
        return worldGeneratorType;
    }

    public void setWorldGeneratorType(WorldGeneratorType worldGeneratorType) {
        this.worldGeneratorType = worldGeneratorType;
    }

    public LocalDateTime getSavedDateTime() {
        return savedDateTime;
    }

    public void setSavedDateTime(LocalDateTime savedDateTime) {
        this.savedDateTime = savedDateTime;
    }
}
