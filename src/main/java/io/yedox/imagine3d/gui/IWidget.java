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
    void render();

    void onClicked(GUIButton sourceButton, PApplet sourceApplet);
    void onInit(PApplet applet);
    void onAfterInit(PApplet applet);
    void onDraw(PApplet applet);
    void onKeyPress(PApplet main);

    void render(PApplet applet);

    void onValueEntered(String value);
}
