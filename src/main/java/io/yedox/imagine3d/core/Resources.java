package io.yedox.imagine3d.core;

import processing.core.PApplet;
import processing.data.JSONObject;

public class Resources {
    private static JSONObject resourceObject;
    private static PApplet applet;

    public static <T> T getResourceValue(Class<T> tClass, String key) {
        resourceObject = applet.loadJSONObject("config/resources.json");
        return (T) resourceObject.get(key);
    }
    public static <T> T getConfigValue(Class<T> tClass, String key) {
        resourceObject = applet.loadJSONObject("config/config.json");
        return (T) resourceObject.get(key);
    }

    public static String getSoundPath(String key) {
        resourceObject = applet.loadJSONObject("config/sounds.json");
        return (String) resourceObject.get(key);
    }

    public static String getTexturePath(String key) {
        resourceObject = applet.loadJSONObject("config/textures.json");
        return (String) resourceObject.get(key);
    }

    public static PApplet getApplet() {
        return applet;
    }

    public static void setApplet(PApplet applet) {
        Resources.applet = applet;
    }
}
