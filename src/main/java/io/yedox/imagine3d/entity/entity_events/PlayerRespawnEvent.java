package io.yedox.imagine3d.entity.entity_events;

import processing.core.PVector;

public class PlayerRespawnEvent {
    private final PVector playerPosition;
    private final int playerHealth;

    public PlayerRespawnEvent(PVector position, int health) {
        this.playerPosition = position;
        this.playerHealth = health;
    }

    public PVector getPlayerPosition() {
        return this.playerPosition;
    }

    public int getPlayerHealth() {
        return this.playerHealth;
    }
}
