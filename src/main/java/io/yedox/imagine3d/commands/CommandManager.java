package io.yedox.imagine3d.commands;

import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
    private static final ArrayList<Command> commands = new ArrayList<>();

    private static String[] generateArgArray(String[] array) {
        String[] temp = new String[array.length - 1];

        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                // skip bruh
            } else {
                temp[i - 1] = array[i];
            }
        }

        return temp;
    }

    public static boolean isCommand(String str) {
        return str.startsWith("/");
    }

    public static boolean checkArg(String[] args, int index) {
        try {
            if (args[index] != null) return true;
            else return false;
        } catch (IndexOutOfBoundsException exception) {
            return false;
        }
    }

    public static String checkArg(int index, String[] args) {
        try {
            if (args[index] != null) return args[index];
            else return "";
        } catch (IndexOutOfBoundsException exception) {
            return "";
        }
    }

    public static void parse(String command, PApplet applet) {
        try {
            String[] cmdSplit = command.split(" ");
            String[] args = generateArgArray(cmdSplit);

            commands.forEach(cmd -> {
                if (cmdSplit[0].equals(cmd.command)) {
                    try {
                        if(cmd.executeLuaClosureAsResult) {
                            cmd.luaClosureResult.call();
                        } else {
                            cmd.commandResult.execute(applet, args);
                        }
                    } catch (MissingArgumentException exception) {
                        Logger.logError("Parse error: " + exception.getMessage() + ". Input: '" + command + "'");
                    }
                }
            });
        } catch (Exception exception) {
            Logger.logError("Parse error: " + exception.getMessage() + ". Input: '" + command + "'");
        }
    }

    public static void addCommand(Command command) {
        commands.add(command);
    }
}
