package io.yedox.imagine3d.gui;

import processing.core.PApplet;

public interface TextBoxInputListener {
    /**
     * Called when the user enters a value
     * into the widget.
     */
    void onValueEntered(String value, GUITextBox textBox, PApplet applet);
}
