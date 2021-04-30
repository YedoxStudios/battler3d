package io.yedox.imagine3d.gui;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static processing.core.PConstants.BACKSPACE;
import static processing.core.PConstants.SHIFT;

public class GUITextBox extends GUIWidget {
    private final PApplet applet;
    private final ArrayList<String> strings = new ArrayList<>();
    public PVector position;
    public PVector dimension;
    public int padding = 5;
    public boolean visible = true;
    private TextBoxInputListener inputListener;
    private String value = "";

    public GUITextBox(PApplet pApplet, int posX, int posY, int width, int height) {
        position = new PVector(posX, posY, 0);
        dimension = new PVector(width, height, 0);
        applet = pApplet;
    }


    private void calculateTextHeight(PApplet applet, String theString, int theWidth) {
        String[] wordsArray = PApplet.split(theString, " ");
        String tempString = "";
        strings.clear();

        for (int i = 0; i < wordsArray.length; i++) {
            if (applet.textWidth(tempString + wordsArray[i]) < theWidth) {
                tempString += wordsArray[i] + " ";
            } else {
                strings.add(tempString.substring(0, tempString.length() - 1));
                tempString = wordsArray[i] + " ";
            }
        }
        strings.add(tempString.substring(0, tempString.length() - 1));
    }

    public void render() {
        if (visible) {
            int textareaWidth = (int) dimension.x;
            calculateTextHeight(applet, value, textareaWidth);


            int textareaHeight = (int) dimension.y;

            this.applet.fill(255, 40);
            this.applet.rect(position.x, position.y, textareaWidth + 5, textareaHeight + 5);
            this.applet.fill(255);


            this.applet.textSize(GUI.FontSize.NORMAL);
            this.applet.fill(50);
            this.applet.text(value, position.x + padding + 4, position.y + padding + 4, textareaWidth, applet.height);
            this.applet.fill(255);
            this.applet.text(value, position.x + padding, position.y + padding, textareaWidth, applet.height);
        }
    }

    @Override
    public void onKeyPress(PApplet main) {
        if (visible) {
            if (main.key == BACKSPACE) {
                if (value.length() > 0) {
                    value = value.substring(0, value.length() - 1);
                }
            } else if (main.key == '\n') {
                this.visible = false;
                this.inputListener.onValueEntered(value, this, applet);
            } else if (main.key == SHIFT || main.key == '\uFFFF') {
                // Do nothing
            } else {
                value += main.key;
            }
        }
    }

    public void addInputListener(TextBoxInputListener textBoxInputListener) {
        inputListener = textBoxInputListener;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String str) {
        this.value = str;
    }
}
