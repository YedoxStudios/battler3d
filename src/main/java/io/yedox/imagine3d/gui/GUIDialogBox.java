package io.yedox.imagine3d.gui;

import io.yedox.imagine3d.core.Resources;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class GUIDialogBox extends GUIWidget {
    public GUILabel dialogTitle;
    public GUIButton defaultButton;

    private boolean visible = false;

    private PVector position;
    private PImage dialogBackgroundTexture;

    public GUIDialogBox(PApplet applet, PVector position, String defaultButtonText) {
        this.defaultButton = new GUIButton(applet);
        this.defaultButton.label.setText(defaultButtonText);
        this.position = position;
        this.dialogBackgroundTexture = applet.loadImage(Resources.getTexturePath("textures.gui.dialog_background"));
    }

    @Override
    public void render(PApplet applet) {
        super.render();
        if(this.isVisible()) {
            applet.image(dialogBackgroundTexture, position.x, position.y);
        }
    }

    public void centerDialog(PApplet applet) {
        position.x = applet.width / 2 - dialogBackgroundTexture.pixelWidth / 2;
        position.y = applet.height / 2 - dialogBackgroundTexture.pixelHeight / 2;
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setTexture(PImage texture) {
        this.dialogBackgroundTexture = texture;
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }
}
