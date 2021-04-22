package io.yedox.imagine3d.gui;

import processing.core.PApplet;
import java.util.EventListener;

/**
 * This file is a part of Battler3D
 * @author Yeppii
 * @description A 3D multiplayer PvP game
 * LICENSE: MIT License (See LICENSE file)
 */


public interface IWidget extends EventListener {
    default void onInit(PApplet applet) {}

    void onClicked(GUIButton sourceButton, PApplet sourceApplet);
    void onAfterInit(PApplet applet);
    void onDraw(PApplet applet);
    void onKeyPress(PApplet main);

    /**
     * Called when the user enters a value
     * into the widget.
     */
    void onValueEntered(String value);

    void render();
    void render(PApplet applet);
}
