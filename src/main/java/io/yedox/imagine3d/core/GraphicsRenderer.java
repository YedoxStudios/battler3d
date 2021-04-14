package io.yedox.imagine3d.core;

import io.yedox.imagine3d.gui.GUI;
import processing.core.PApplet;

public class GraphicsRenderer {
    private final PApplet pApplet;

    public GraphicsRenderer(PApplet applet) {
        this.pApplet = applet;
    }

    public void drawShadowedText(String text, int x, int y, int textSize, boolean centerVert) {
        pApplet.pushMatrix();
        pApplet.textSize(GUI.FontSize.MEDIUM);
        pApplet.fill(100);
        pApplet.text(text, x - (centerVert ? (((int) pApplet.textWidth(text)) >> 1) : 0), y);
        pApplet.fill(255);
        pApplet.text(text, x - (centerVert ? (((int) pApplet.textWidth(text)) >> 1) : 0) - 3, y - 3);
        pApplet.popMatrix();
    }
}
