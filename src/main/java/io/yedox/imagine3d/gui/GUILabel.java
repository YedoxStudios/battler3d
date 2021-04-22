package io.yedox.imagine3d.gui;

import io.yedox.imagine3d.core.Game;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;
import processing.core.PConstants;

import static io.yedox.imagine3d.gui.GUI.FontSize;

public class GUILabel extends GUIWidget {
    public final int shadowOffset = 3;

    public boolean hudWidget = false;
    public boolean drawShadow;
    public boolean deathScreenWidget;
    public boolean visible;

    public int x = 0;
    public int y = 0;

    public int cR = 0;
    public int cG = 0;
    public int cB = 0;


    public String text;
    public int fontSize;
    public Game.Screen screen = Game.Screen.MAIN_GAME_SCREEN;

    public GUILabel(PApplet applet, String text, int px, int py, boolean shadow) {
        this.fontSize = FontSize.NORMAL;
        this.text = text;
        this.visible = true;
        this.drawShadow = shadow;
        this.deathScreenWidget = false;
        this.cR = 0;
        this.cG = 0;
        this.cB = 0;
        this.x = px;
        this.y = py;
        this.onInit(applet);
    }

    public GUILabel(PApplet applet, String text, int px, int py, boolean shadow, int r, int g, int b) {
        this.fontSize = FontSize.NORMAL;
        this.text = text;
        this.visible = true;
        this.drawShadow = shadow;
        this.deathScreenWidget = false;
        this.cR = r;
        this.cG = g;
        this.cB = b;
        this.x = px;
        this.y = py;
        this.onInit(applet);
    }

    public void render(PApplet applet) {
        if (visible) {
            applet.textSize(fontSize);

            if (drawShadow) {
                applet.fill(100);
                applet.text(text, x + shadowOffset, y + shadowOffset);
            }
            applet.fill(cR, cG, cB);
            applet.text(text, x, y);
            this.onDraw(applet);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void onDraw(PApplet sourceApplet) {
        super.onDraw(sourceApplet);
        if (sourceApplet.mousePressed && sourceApplet.keyPressed && sourceApplet.key == PConstants.CODED && sourceApplet.keyCode == PConstants.CONTROL) {
            this.x = sourceApplet.mouseX;
            this.y = sourceApplet.mouseY;
            Logger.logDebug("X: " + x + " Y: " + y);
        }
    }
}
