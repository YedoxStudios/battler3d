package i3lua.utils;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class Logger extends TwoArgFunction {

    public Logger() {}

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("debugln", new debugln());
        library.set("errorln", new errorln());
        env.set("logger", library);
        return library;
    }

    static class debugln extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            io.yedox.imagine3d.utils.Logger.logLuaScriptDebug(x.checkstring());
            return LuaValue.valueOf(true);
        }
    }

    static class errorln extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            io.yedox.imagine3d.utils.Logger.logLuaScriptError(x.checkstring());
            return LuaValue.valueOf(true);
        }
    }
}
