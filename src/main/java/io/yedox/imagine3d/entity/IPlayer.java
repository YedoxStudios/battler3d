package io.yedox.imagine3d.entity;

import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import processing.core.PApplet;

public interface IPlayer {
    void onPlayerRespawn(PlayerRespawnEvent respawnEvent);
    void onPlayerHurt();

    void onPlayerDie();

    void onKeyPress(PApplet applet);

    void onMousePress(PApplet applet, int button);
}
