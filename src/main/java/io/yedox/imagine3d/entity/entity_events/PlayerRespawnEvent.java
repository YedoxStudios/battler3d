package io.yedox.imagine3d.entity.entity_events;

import processing.core.PVector;

public class PlayerRespawnEvent {
    private PVector playerPosition;
    private PVector playerDimensions;

    private int playerHealth;

    public PlayerRespawnEvent(PVector position, PVector dimensions, int health) {
        this.playerPosition = position;
        this.playerDimensions = dimensions;
        this.playerHealth = health;
    }

    public PVector getPlayerPosition() {
        return playerPosition;
    }

    public PVector getPlayerDimensions() {
        return playerDimensions;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }
}
