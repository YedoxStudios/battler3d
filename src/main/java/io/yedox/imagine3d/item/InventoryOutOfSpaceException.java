package io.yedox.imagine3d.item;

public class InventoryOutOfSpaceException extends Exception {

    public InventoryOutOfSpaceException() {
    }

    public InventoryOutOfSpaceException(String cause) {
        super(cause);
    }
}
