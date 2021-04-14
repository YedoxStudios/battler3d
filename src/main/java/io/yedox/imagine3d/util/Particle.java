package io.yedox.imagine3d.util;

// A simple Particle class

import io.yedox.imagine3d.gui.GUI;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Particle {
    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public PVector dimension;

    private final PImage texture;

    private final PApplet applet;
    private float lifespan;

    public Particle(PVector l, PApplet applet) {
        acceleration = new PVector(0, 0.05f);
        velocity = new PVector(applet.random(-1, 1), applet.random(-2, 0));
        position = l.copy();
        dimension = new PVector(applet.random(1, 2), applet.random(1, 2));
        lifespan = 255.0f;
        texture = applet.loadImage("textures/blocks/dirt.png");
        this.applet = applet;
    }

    public void run() {
        update();
        display();
    }

    // Method to update position
    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
        lifespan -= 1.0;
    }

    // Method to display
    public void display() {
        PVector rotation = Utils.getRotationFacing(this.position, GUI.getPlayer().position);

        applet.pushMatrix();

        if (rotation != null) {
            applet.rotateX((-rotation.x));
        }

        if (rotation != null) {
            applet.rotateY(rotation.y);
        }

        applet.image(texture, position.x - 1, position.y, dimension.x, dimension.y);
        applet.popMatrix();
    }

    // Is the particle still useful?
    public boolean isDead() {
        return lifespan < 0.0;
    }
}