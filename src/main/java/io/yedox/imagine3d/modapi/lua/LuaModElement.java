package io.yedox.imagine3d.modapi.lua;

import io.yedox.imagine3d.utils.ANSIConstants;
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
    private String luaFile;

    public LuaModElement(String file, PApplet applet) {
        try {
            this.luaFile = file;
            this.globals = JsePlatform.standardGlobals();
            StringBuffer sb = new StringBuffer();
            this.strings = applet.loadStrings(file);
            for(int i = 0; i < strings.length; i++) {
                sb.append(strings[i]).append("\n");
            }
            this.luaScript = sb.toString();
            this.chunk = this.globals.load(this.luaScript);
        } catch (NullPointerException nullPointerException) {
            Logger.logError("Could not load file: '" + this.luaFile + "'");
        }
    }

    public void execute() {
        try {
            this.chunk.call();
        } catch (Exception exception) {
            Logger.logError("Lua compile error: '" + exception.getMessage() + "'");
        }

//        Logger.logDebug("Running script... ['file': '" + this.luaFile + "', 'contents': '\n" + ANSIConstants.ANSI_YELLOW + this.luaScript + "']");
    }
}
