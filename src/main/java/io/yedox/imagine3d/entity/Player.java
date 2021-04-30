package io.yedox.imagine3d.entity;

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.Resources;
import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Camera;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.Random;

public class Player extends Camera implements IPlayer {
    public final boolean observerMode;

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

        this.speed = 0.13f;
        this.dimensions = new PVector(2, 20, 2);
        this.velocity = new PVector(0, 0, 0);
        this.gravity = new PVector(0, 0.05f, 0);
        this.grounded = false;
        this.dead = false;
        this.flyMode = false;
        this.sensitivity = 0.5f;
        this.position = new PVector(10, -30, 10);
        this.username = "Player" + new Random().nextInt(100);
        this.deathCause = "";
        this.health = 8;
        this.maxHealth = 8;
        this.observerMode = Resources.getConfigValue(Boolean.class, "player.observerMode");

        this.initEntityData();

        if(Game.developerDebugModeEnabled) Logger.logDebug("PlayerEntity's JSONData: \n" + entityData.toJson());
    }

    public void initEntityData() {
        entityData.putKey("playerSpeed", speed);
        entityData.putKey("isDead", dead);
        entityData.putKey("health", health);
        entityData.putKey("isGrounded", grounded);
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

                if (grounded && applet.keyPressed && applet.key == ' ' && !GUI.chatBox.visible) {
                    grounded = false;
                    velocity.y = -1.9f;
                    position.y -= 1.0;
                }

                if (position.y >= 300 && !observerMode) {
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
        position.x = applet.random(1, GUI.terrainManager.blockSize * 5);
        position.z = applet.random(1, GUI.terrainManager.blockSize * 5);
        velocity.y = 0;
        health = 8;

        GUI.client.sendMessage("{\"respawned\": true, \"username\": \"" + username + "\"}");

        this.setDead(false);
    }

    @Override
    public void onPlayerHurt() {

    }

    @Override
    public synchronized void onPlayerDie() {
        this.setControllable(!dead);

        GUI.client.sendMessage("{\"dead\": true,\"username\": \"" + username + "\",\"deathCause\": \"" + deathCause + "\"}");
        Logger.logDebug("Player '" + username + "' died.");

        int windowWidth = window.getX() + applet.width / 2;
        int windowHeight = window.getY() + applet.height / 2;

        robot.mouseMove(windowWidth, windowHeight);
    }

    @Override
    public void onKeyPress(PApplet applet) {
        if (applet.key == 'g' && !GUI.chatBox.visible) {
            hurt(2, "died");
        }
    }

    @Override
    public void onMousePress(PApplet applet, int button) {

    }
}
