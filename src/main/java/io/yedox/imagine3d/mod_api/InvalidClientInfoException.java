package io.yedox.imagine3d.mod_api;

public class InvalidClientInfoException extends Exception {
    public InvalidClientInfoException() {
        this.initCause(new Throwable("Invalid Mod Client Info Provided."));
    }
}
