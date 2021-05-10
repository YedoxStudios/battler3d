package io.yedox.imagine3d.entity;

import processing.core.PVector;

public class SerializablePlayer {
    public PVector position;
    public String username;

    public SerializablePlayer(PVector position, String username) {
        this.position = position;
        this.username = username;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
