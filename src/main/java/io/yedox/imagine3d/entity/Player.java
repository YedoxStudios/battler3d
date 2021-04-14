package io.yedox.imagine3d.entity;

import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.util.Camera;
import io.yedox.imagine3d.util.Logger;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Random;

public class Player extends Camera implements IPlayer {
    private final boolean observerMode;
    public PVector dimensions;
    public PVector velocity;
    public PVector gravity;
    public boolean flyMode;
    public boolean grounded;
    public int health;
    public int maxHealth;

    public String username;
    public String deathCause;
    private boolean dead;
    private int nextHurtTimer = 0;

    public Player(PApplet applet) {
        super(applet);
        speed = 0.23f;
        dimensions = new PVector(2, 20, 2);
        velocity = new PVector(0, 0, 0);
        gravity = new PVector(0, 0.09f, 0);
        grounded = false;
        dead = false;
        flyMode = false;
        sensitivity = 0.5f;
        position = new PVector(10, -30, 10);
        username = "Player" + new Random().nextInt(100);
        deathCause = "";
        health = 8;
        maxHealth = 8;
        observerMode = Resources.getConfigValue(Boolean.class, "player.observerMode");
    }

    // Keep this method synchronized
    public synchronized void update(PApplet applet) {
        this.setControllable(!dead);

        if (health < 0) {
            health = 0;
        }

        if (health == 0 && !dead) {
            this.setDead(true);
            this.onPlayerDie();
        }

        if (!dead) {
            if (!observerMode) {
                velocity.add(gravity);
                position.add(velocity);
                if (grounded && applet.keyPressed && applet.key == ' ') {
                    grounded = false;
                    velocity.y = -1.9f;
                    position.y -= 1.0;
                }
                if (position.y >= 300) {
                    if (nextHurtTimer > 50) {
                        hurt(3, "fell off the platform");
                        nextHurtTimer = 0;
                    }
                    nextHurtTimer++;
                }
            }
        }
    }

    public void hurt(int amount, String hurtCause) {
        this.onPlayerHurt();
        deathCause = hurtCause;
        if (amount < 0)
            Logger.logError("Damage amount should be greater than 0");
        health -= amount;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean bool) {
        this.dead = bool;
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent respawnEvent) {
        Logger.logDebug("Player '" + username + "' respawned.");

        position.y = -50;
        position.x = position.z = 10;
        velocity.y = 0;
        health = 8;

        GUI.client.sendMessage("{\"respawned\": true, \"username\": \"" + username + "\"}");

        this.setDead(false);
    }

    @Override
    public void onPlayerHurt() {

    }

    @Override
    public void onPlayerDie() {
        GUI.client.sendMessage("{\"dead\": true,\"username\": \"" + username + "\",\"deathCause\": \"" + deathCause + "\"}");

        Logger.logDebug("Player '" + username + "' died.");
    }

    @Override
    public void onKeyPress(PApplet applet) {
        if (applet.key == 'g') {
            hurt(2, "died");
        }
    }

    @Override
    public void onMousePress(PApplet applet, int button) {

    }
}
