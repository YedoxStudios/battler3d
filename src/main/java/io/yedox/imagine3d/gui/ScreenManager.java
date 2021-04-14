package io.yedox.imagine3d.gui;

import java.util.ArrayList;

public class ScreenManager {
    private static ArrayList<GUIScreen> guiScreenArrayList = new ArrayList<GUIScreen>();

    public static void addScreen(GUIScreen screen) {
        guiScreenArrayList.add(screen);
    }

    public static GUIScreen getScreen(int index) {
        return guiScreenArrayList.get(index);
    }
}