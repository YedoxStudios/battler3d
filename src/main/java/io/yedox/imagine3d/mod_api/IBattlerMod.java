package io.yedox.imagine3d.mod_api;

import io.yedox.imagine3d.entity.entity_events.PlayerRespawnEvent;
import processing.core.PApplet;

public interface IBattlerMod {
    /**
     * This method is called when the modloader
     * finishes loading the classes from the JAR
     *
     * @param applet A reference variable to the
     *               main game applet
     */
    void initMod(PApplet applet);

    /**
     * This method is called when the game
     * draws the main menu
     *
     * @param applet A reference variable to the
     *               main game applet
     */
    void onMenuDraw(PApplet applet);

    /**
     * This method is called when the player
     * dies
     *
     * @param applet A reference variable to the
     *               main game applet
     * @param playerRespawnEvent The player respawn event
     */
    void onPlayerDie(PApplet applet, PlayerRespawnEvent playerRespawnEvent);

    default void initMod() {

    }
}
