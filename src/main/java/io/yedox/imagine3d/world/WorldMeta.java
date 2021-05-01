package io.yedox.imagine3d.world;

import java.time.LocalDateTime;

public class WorldMeta {
    private String worldName;
    private String savedVersion;
    private WorldGeneratorType worldGeneratorType;
    private LocalDateTime savedDateTime;

    public WorldMeta(String worldName, String savedVersion, WorldGeneratorType generatorType, LocalDateTime localDateTime) {
        this.worldName = worldName;
        this.savedVersion = savedVersion;
        this.worldGeneratorType = generatorType;
        this.savedDateTime = localDateTime;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getSavedVersion() {
        return savedVersion;
    }

    public void setSavedVersion(String savedVersion) {
        this.savedVersion = savedVersion;
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
