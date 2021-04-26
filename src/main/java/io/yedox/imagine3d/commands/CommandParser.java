package io.yedox.imagine3d.commands;

import i3lua.LuaModElement;
import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;

public class CommandParser {
    public static boolean isCommand(String str) {
        return str.startsWith("/");
    }

    public static void parse(String command, PApplet applet) {
        try {
            if(command.startsWith("/lua exec")) {
                String[] split = command.split(" ");
                GUI.luaModElement = new LuaModElement(split[1], applet);
                GUI.luaModElement.execute();
            }
        } catch (Exception exception) {
            Logger.logError("Error: " + exception.getMessage());
        }
    }
}
