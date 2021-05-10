package io.yedox.imagine3d.entity;

import io.yedox.imagine3d.core.EntityData;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.io.Serializable;

public class Entity implements IEntity, Serializable {
    public PVector position = new PVector();
    public PVector size = new PVector();
    public PImage texture;

    public float speed = 1f;
    public EntityData entityData;

    public Entity() {
        entityData = new EntityData();
    }

    public EntityData getEntityData() {
        return entityData;
    }

    public Entity setEntityData(EntityData entityData) {
        this.entityData = entityData;
        return this;
    }

    public Entity setSize(PVector size) {
        this.size = size;
        return this;
    }

    public Entity setTexture(PImage texture) {
        this.texture = texture;
        return this;
    }

    public Entity setPosition(PVector position) {
        this.position = position;
        return this;
    }

    public Entity setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

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
    }
}
