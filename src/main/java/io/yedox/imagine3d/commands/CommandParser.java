package io.yedox.imagine3d.commands;

import io.yedox.imagine3d.gui.GUI;
import io.yedox.imagine3d.utils.Logger;

public class CommandParser {
    public static boolean isCommand(String str) {
        return str.startsWith("/");
    }

    public static void parse(String command) {
        String[] split = command.split(" ");

        if(split[0].equals("/entitydata")) {
            if(split[1].equals("get")) {
                if(split[2].equals("$player")) {
                    Logger.logDebug(GUI.player.entityData.toJson());
                }
            }
        } else if (split[0].equals("/username")) {
            if (split[1].equals("set")) {
                GUI.player.username = split[2];
            }
        }
    }
}
