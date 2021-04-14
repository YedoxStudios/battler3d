package io.yedox.imagine3d.util;

// A class to describe a group of Particles
// An ArrayList is used to manage the list of Particles

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class ParticleSystem {
    public ArrayList<Particle> particles;
    public PVector origin;
    public PApplet applet;

    public ParticleSystem(PVector position, PApplet applet) {
        origin = position.copy();
        particles = new ArrayList<>();
        this.applet = applet;
    }

    public void addParticle() {
        particles.add(new Particle(origin, applet));
    }

    public void run() {
        for (int i = particles.size()-1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.run();
            if (p.isDead()) {
                particles.remove(i);
            }
        }
    }
}