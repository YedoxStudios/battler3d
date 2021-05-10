package i3lua.core;

import io.yedox.imagine3d.commands.CommandBuilder;
import io.yedox.imagine3d.commands.CommandManager;
import io.yedox.imagine3d.gui.GUI;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class Game extends TwoArgFunction {
    public Game() {}

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("execute_command", new execute_command());
        library.set("add_command", new add_command());
        library.set("toggle_observer_mode", new toggle_observer_mode());
        library.set("set_player_username", new set_player_username());
        env.set("game", library);
        return library;
    }

    static class execute_command extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            if (!x.isnil() && x.isstring() && x.checkjstring().length() > 0) {
                GUI.player.username = x.checkjstring();
                return LuaValue.valueOf(true);
            } else {
                return LuaValue.valueOf(false);
            }
        }
    }

    static class add_command extends TwoArgFunction {
        public LuaValue call(LuaValue x, LuaValue y) {
            if(x.isstring() && y.isfunction()) {
                CommandManager.addCommand(CommandBuilder.createCommand(x.checkjstring()).executesClosure(y.checkclosure()));
                return LuaValue.valueOf(true);
            } else {
                return LuaValue.valueOf(false);
            }
        }
    }

    static class set_player_username extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            if (!x.isnil() && x.isstring() && x.checkjstring().length() > 0) {
                GUI.player.username = x.checkjstring();
                return LuaValue.valueOf(true);
            } else {
                return LuaValue.valueOf(false);
            }
        }
    }
    static class toggle_observer_mode extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.cosh(x.checkdouble()));
        }
    }
}
