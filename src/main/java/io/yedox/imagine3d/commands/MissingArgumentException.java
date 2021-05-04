package io.yedox.imagine3d.commands;

public class MissingArgumentException extends Exception {
    private int missingArgIndex;
    private String argDetails;

    public MissingArgumentException(int missingArgumentIndex) {
        this.missingArgIndex = missingArgumentIndex;
    }

    public MissingArgumentException(int missingArgumentIndex, String argumentDetails) {
        this.missingArgIndex = missingArgumentIndex;
        this.argDetails = argumentDetails;
    }

    @Override
    public String getMessage() {
        return "Argument '" + this.missingArgIndex + "' doesn't exist. " + this.argDetails;
    }
}
