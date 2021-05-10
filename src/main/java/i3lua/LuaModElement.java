package i3lua;

import i3lua.core.Game;
import io.yedox.imagine3d.utils.ANSIConstants;
import io.yedox.imagine3d.utils.Logger;
import io.yedox.imagine3d.utils.Utils;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JsePlatform;
import processing.core.PApplet;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.FileReader;

public class LuaModElement {
    private boolean debugEnabled = false;

    private Globals globals;
    private LuaValue chunk;
    private String[] strings;
    private String luaScript;
    private String luaFile;

    private ScriptEngine engine;
    private ScriptEngineFactory engineFactory;
    private ScriptEngineManager engineManager;

    public LuaModElement(String file, PApplet applet) {
        try {
            this.luaFile = file;

            this.engineManager = new ScriptEngineManager();
            this.engine = engineManager.getEngineByName("luaj");
            this.engineFactory = engine.getFactory();

            this.globals = JsePlatform.standardGlobals();
            this.globals.load(new JseBaseLib());
            this.globals.load(new PackageLib());
            this.globals.load(new StringLib());
            this.globals.load(new Game());
            this.globals.load(new i3lua.utils.Logger());

            LoadState.install(this.globals);
            LuaC.install(this.globals);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            // delete the last new line separator
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();

            this.luaScript = stringBuilder.toString();
            this.chunk = this.globals.load(this.luaScript);
        } catch (Exception exception) {
            Logger.logLuaError("Could not load file: '" + this.luaFile + "'. Reason:\n" + exception.getMessage());
        }
    }

    public void execute() {
        try {
            this.chunk.call();
        } catch (Exception exception) {
            Logger.logLuaError("Lua compile error: '" + exception.getMessage() + "'");
        }

        if (debugEnabled)
            Logger.logDebug("Running script... ['file': '" + this.luaFile + "', 'contents': '\n" + ANSIConstants.ANSI_YELLOW + this.luaScript + "']");
    }

    public void printEngineInfo() {
        Logger.logLuaDebug("Engine name: " + engineFactory.getEngineName());
        Logger.logLuaDebug("Engine Version: " + engineFactory.getEngineVersion());
        Logger.logLuaDebug("Language Name: " + engineFactory.getLanguageName());
        Logger.logLuaDebug("Language Version: " + engineFactory.getLanguageVersion());
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
