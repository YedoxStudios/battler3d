package io.yedox.imagine3d.gui;

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.core.SoundRegistry;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.utils.Utils;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This file is a part of Battler3D
 *
 * @author Yeppii
 * @description A very small Minecraft-themed PvP game
 * LICENSE: MIT License (See LICENSE file)
 */


public class GUIButton extends GUIWidget {
    public int colorR;
    public int colorG;
    public int colorB;

    public PImage textureNormal;
    public PImage texturePressed;
    public PImage textureHover;

    public int width = 200;
    public int height = width / 4;
    public int x = 0;
    public int y = 0;

    public boolean mouseOver = false;
    public boolean mousePressed = false;
    public boolean visible = true;
    public boolean hudWidget = false;
    public boolean deathScreenWidget = false;
    public boolean pauseScreenWidget = false;

    public GUILabel label;
    public String text = "Button";
    public Game.Screen screen = Game.Screen.MENU_SCREEN;

    public GUIButton(PApplet applet) {
        this.x = applet.width / 2;
        this.y = applet.height / 2;
        this.textureNormal = applet.loadImage("textures/gui/button.png");
        this.texturePressed = applet.loadImage("textures/gui/button_press.png");
        this.textureHover = applet.loadImage("textures/gui/button_hover.png");
        this.onInit(applet);
        this.label = new GUILabel(applet, text, this.x, this.y, true, 255, 255, 255);
        this.onAfterInit(applet);
        this.label.x = this.x + this.width / 2 - ((int) applet.textWidth(this.label.getText())) / 2;
        this.label.y = this.y + this.height / 2 + 7;
        this.label.fontSize = GUI.FontSize.NORMAL;
    }

    public GUIButton(PApplet applet, String text) {
        this.x = applet.width / 2;
        this.y = applet.height / 2;
        this.textureNormal = applet.loadImage("textures/gui/button.png");
        this.texturePressed = applet.loadImage("textures/gui/button_press.png");
        this.textureHover = applet.loadImage("textures/gui/button_hover.png");
        this.onInit(applet);
        this.label = new GUILabel(applet, text, this.x, this.y, true, 255, 255, 255);
        this.onAfterInit(applet);
        this.label.x = this.x + this.width / 2 - ((int) applet.textWidth(this.label.getText())) / 2;
        this.label.y = this.y + this.height / 2 + 7;
        this.label.fontSize = GUI.FontSize.NORMAL;
        this.label.setText(text);
    }


    public void render(PApplet applet) {

        if (visible) {
            mouseOver = Utils.overRect(x, y, width, height, applet);

            if (mouseOver) {
                applet.image(textureHover, x, y, width, height);
                applet.fill(150);
                label.cG = label.cR = 255;
                label.cB = 0;
            } else {
                applet.image(textureNormal, x, y, width, height);
                applet.fill(100);
                label.cB = label.cG = label.cR = 255;
            }

            if (mousePressed && mouseOver) {
                applet.image(texturePressed, x, y, width, height);
                applet.fill(1);
            }

            applet.fill(255);
            label.render(applet);
            applet.fill(255);
        }
        this.onDraw(applet);
    }

    public void reloadResources(PApplet applet) {
        textureNormal = applet.loadImage("textures/gui/button.png");
        texturePressed = applet.loadImage("textures/gui/button_press.png");
        textureHover = applet.loadImage("textures/gui/button_hover.png");
    }

    public synchronized void onMousePressed(PApplet applet) {
        mousePressed = true;
        if (Utils.overRect(this.x, this.y, this.width, this.height, applet) && visible) {
            // is this line of code should be in the onClicked() function?
            SoundRegistry.playSound(SoundRegistry.Sounds.GUI_CLICK);
            this.onClick(this, applet);
        }
    }

    public void onMouseReleased(PApplet applet) {
        mousePressed = false;
    }

    public void setColor(int r, int g, int b) {
        colorR = r;
        colorG = g;
        colorB = b;
    }

    @Override
    public void onClick(GUIButton sourceButton, PApplet sourceApplet) {
        Logger.logDebug("Event: " + this.getClass().getSimpleName() + "onButtonClicked()");
    }
}
