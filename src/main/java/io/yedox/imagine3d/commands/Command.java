package io.yedox.imagine3d.commands;

import java.util.Arrays;
import java.util.List;

public class Command {
    public String command;
    public List<String> commandArgs;
    public CommandResult commandResult;

    public Command(String command, String[] commandArgs, CommandResult commandResult) {
        this.command = !CommandManager.isCommand(command) ? "/" + command : command;
        this.commandArgs = Arrays.asList(commandArgs);
        this.commandResult = commandResult;
    }

    public Command addArgument(String argument) {
        this.commandArgs.add(argument);
        return this;
    }

    public Command executes(CommandResult result) {
        this.commandResult = result;
        return this;
    }
}
