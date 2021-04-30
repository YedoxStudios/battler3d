package io.yedox.imagine3d.commands;

public class CommandBuilder {
    public static Command createCommand(String command) {
        return new Command(command, new String[]{}, (appletCtx, args) -> {});
    }
}
