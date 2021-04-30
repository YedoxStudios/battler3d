package io.yedox.imagine3d.core;

import io.yedox.imagine3d.entity.Entity;
import io.yedox.imagine3d.utils.Logger;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    public static List<Entity> entityList = new ArrayList<>();
    public static JSONObject versionInfo;
    public static String releaseVersion;
    public static String releaseName;
    public static HashMap<String, String> gameConfigValues = new HashMap<>();
    public static boolean multiplayerEnabled;
    public static boolean developerDebugModeEnabled;
    public static Screen currentScreen = Screen.MENU_SCREEN;
    private static int songsPlayed = 0;
    private static int songsTimer = 0;
    private static boolean beta = false;

    public static int getSongsPlayed() {
        return songsPlayed;
    }

    public static void setSongsPlayed(int songsPlayed) {
        Game.songsPlayed = songsPlayed;
    }

    public static int getSongsTimer() {
        return songsTimer;
    }

    public static void setSongsTimer(int songsTimer) {
        Game.songsTimer = songsTimer;
    }

    public static boolean isGameOver() {
        return false;
    }

    public static boolean isBeta() {
        return beta;
    }

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public static void setCurrentScreen(Screen screen) {
        currentScreen = screen;
    }

    public static void loadResources(PApplet applet) {
        Logger.logDebug("Loading version info..");

        // Load version info
        versionInfo = applet.loadJSONObject("config/version_info.json");

        releaseVersion = versionInfo.getString("version.release_version");
        releaseName = versionInfo.getString("version.release_name");

        multiplayerEnabled = Resources.getConfigValue(Boolean.class, "multiplayer.enabled");
        beta = versionInfo.getBoolean("version.is_beta");
    }

    public static void initEntities(PApplet applet) {
        Logger.logDebug("Initializing entity list....");
    }

    public static void initGameConfigValues() {
        gameConfigValues.put("lightsEnabled", Resources.getConfigValue(Boolean.class, "world.lightsEnabled").toString());
    }

    public static void drawEntities(PApplet applet) {
        entityList.forEach(entity -> entity.draw(entity, applet));
    }

    public enum Screen {
        MENU_SCREEN,
        MAIN_GAME_SCREEN,
        ABOUT_SCREEN,
        OPTIONS_SCREEN,
        TERRAINGEN_SCREEN
    }

    public static class ResourceManager {
        public PImage getImage(String loc, PApplet src) {
            return src.loadImage(loc);
        }
    }
}
