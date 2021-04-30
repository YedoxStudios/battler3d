package io.yedox.imagine3d.commands;

public class MissingArgumentException extends Exception {
    private int missingArgIndex;

    public MissingArgumentException(int missingArgumentIndex) {
        this.missingArgIndex = missingArgumentIndex;
    }

    @Override
    public String getMessage() {
        return "Argument '" + missingArgIndex + "' doesn't exist.";
    }
}
