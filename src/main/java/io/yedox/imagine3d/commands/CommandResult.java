package io.yedox.imagine3d.commands;

import processing.core.PApplet;

public interface CommandResult {
    void execute(PApplet appletCtx, String[] args) throws MissingArgumentException;
}
