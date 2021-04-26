package i3lua.utils;

import io.yedox.imagine3d.gui.GUI;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class Logger extends TwoArgFunction {

    public Logger() {}

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("logDebug", new logDebug());
        library.set("logError", new logError());
        env.set("Logger", library);
        return library;
    }

    static class logDebug extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            io.yedox.imagine3d.utils.Logger.logLuaDebug(x.checkstring());
            return LuaValue.valueOf(true);
        }
    }

    static class logError extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            io.yedox.imagine3d.utils.Logger.logLuaError(x.checkstring());
            return LuaValue.valueOf(true);
        }
    }
}
