package io.yedox.imagine3d.entity;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Entity implements IEntity {
    public PVector position = new PVector();
    public PVector size = new PVector();
    public PImage texture;
    public int entitySpeed = 1;

    @Override
    public void onEntityInit(Entity entity, PApplet applet) {
    }

    @Override
    public void onEntityHurt(Entity sourceEntity, Entity targetEntity) {
    }

    @Override
    public void onEntityDestroyed(Entity entity) {
    }

    @Override
    public void draw(Entity entity, PApplet applet) {
    }

    @Override
    public void onKeyPressed(PApplet applet) {
        switch (applet.key) {
            case 'w':
                this.position.y-=entitySpeed;
                break;
            case 'a':
                this.position.x-=entitySpeed;
                break;
            case 's':
                this.position.y+=entitySpeed;
                break;
            case 'd':
                this.position.x+=entitySpeed;
                break;
        }
    }
}