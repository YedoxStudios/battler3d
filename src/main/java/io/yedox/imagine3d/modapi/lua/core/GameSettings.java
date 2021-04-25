package io.yedox.imagine3d.modapi.lua.core;

import io.yedox.imagine3d.gui.GUI;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class GameSettings extends TwoArgFunction {

    public GameSettings() {}

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("setPlayerUsername", new setPlayerUsername());
        library.set("setIsPlayerObserverMode", new setIsPlayerObserverMode());
        env.set("GameSettings", library);
        return library;
    }

    static class setPlayerUsername extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            if (!x.isnil() && x.isstring() && x.checkjstring().length() > 0) {
                GUI.player.username = x.checkjstring();
                return LuaValue.valueOf(true);
            } else {
                return LuaValue.valueOf(false);
            }
        }
    }

    static class setIsPlayerObserverMode extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.cosh(x.checkdouble()));
        }
    }
}
