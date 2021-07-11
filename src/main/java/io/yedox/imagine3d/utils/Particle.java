package io.yedox.imagine3d.utils;

// A simple Particle class

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Particle {
    private final PImage texture;
    private final PApplet applet;
    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public PVector dimensions;
    public PVector rotation;
    private float lifespan;

    private float rx, ry, rz;
    private float vx, vy, vz;

    public Particle(PVector l, PApplet applet) {
        this.acceleration = new PVector(0, 0.05f);
        this.velocity = new PVector(applet.random(-0.5f, 1), applet.random(-1, 0), applet.random(-0.5f, 1));
        this.position = l.copy();
        this.dimensions = new PVector(applet.random(0.3f, 0.5f), applet.random(0.3f, 0.5f), applet.random(0.3f, 0.5f));
        this.rotation = new PVector(applet.random(-1, 1),applet.random(-1, 1),applet.random(-1, 1));
        this.lifespan = 255.0f;
        this.texture = applet.loadImage("textures/blocks/platform.png");
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

//        float rxp = velocity.x;
//        float ryp = velocity.y;
//        float rzp = velocity.z;
//        rx = (float) ((rx * 0.9) + (rxp * 0.1));
//        ry = (float) ((ry * 0.9) + (ryp * 0.1));
//        rz = (float) ((ry * 0.9) + (rzp * 0.1));
//        position.x += rx;
//        position.y += ry;
//        position.z += rz;

        lifespan -= 1.0;
    }

    // Method to display
    public void display() {
        applet.pushMatrix();
        applet.noLights();
        applet.translate(position.x -1, position.y, position.z);
        applet.rotateX(rotation.x);
        applet.rotateY(rotation.y);
        applet.rotateZ(rotation.z);
        applet.scale(dimensions.x, dimensions.y, dimensions.z);
        cubeVertex(texture);
        applet.lights();
        applet.popMatrix();
    }

    private void cubeVertex(PImage texture) {
        applet.beginShape(PConstants.QUADS);
        applet.texture(texture);

        // +Z "front" face

        applet.vertex(-1, -1, 1, 0, 0);
        applet.vertex(1, -1, 1, 1, 0);
        applet.vertex(1, 1, 1, 1, 1);
        applet.vertex(-1, 1, 1, 0, 1);


        // -Z "back" face
        applet.vertex(1, -1, -1, 0, 0);
        applet.vertex(-1, -1, -1, 1, 0);
        applet.vertex(-1, 1, -1, 1, 1);
        applet.vertex(1, 1, -1, 0, 1);
        // +Y "bottom" face
        applet.vertex(-1, 1, 1, 0, 0);
        applet.vertex(1, 1, 1, 1, 0);
        applet.vertex(1, 1, -1, 1, 1);
        applet.vertex(-1, 1, -1, 0, 1);
        // -Y "top" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(1, -1, -1, 1, 0);
        applet.vertex(1, -1, 1, 1, 1);
        applet.vertex(-1, -1, 1, 0, 1);
        applet.vertex(1, -1, 1, 0, 0);
        applet.vertex(1, -1, -1, 1, 0);
        applet.vertex(1, 1, -1, 1, 1);
        applet.vertex(1, 1, 1, 0, 1);
        // -X "left" face
        applet.vertex(-1, -1, -1, 0, 0);
        applet.vertex(-1, -1, 1, 1, 0);
        applet.vertex(-1, 1, 1, 1, 1);
        applet.vertex(-1, 1, -1, 0, 1);

        applet.endShape();
    }

    // Is the particle still useful?
    public boolean isDead() {
        return lifespan < 0.0;
    }
}
