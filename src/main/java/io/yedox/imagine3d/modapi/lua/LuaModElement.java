package io.yedox.imagine3d.modapi.lua;

import io.yedox.imagine3d.utils.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import processing.core.PApplet;

public class LuaModElement {
    private Globals globals;
    private LuaValue chunk;
    private String[] strings;
    private String luaScript;

    public LuaModElement(String file, PApplet applet) {
        try {
            globals = JsePlatform.standardGlobals();
            StringBuffer sb = new StringBuffer();
            strings = applet.loadStrings(file);
            for(int i = 0; i < strings.length; i++) {
                sb.append(strings[i]).append("\n");
            }
            luaScript = sb.toString();
            chunk = globals.load(luaScript);
        } catch (NullPointerException nullPointerException) {
            Logger.logError("Could not load file: '" + file + "'");
        }
    }

    public void execute() {
        try {
            chunk.call();
        } catch (Exception exception) {
            Logger.logError("Lua compile error: '" + exception.getMessage() + "'");
        }
        Logger.logDebug("Script: " + luaScript);
    }
}
