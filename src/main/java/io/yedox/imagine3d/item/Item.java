package io.yedox.imagine3d.item;

public class Item {
    private ItemType itemType;
    private boolean stackable;
    private int maxItemStackSize;

    public Item(ItemType itemType) {
        this.itemType = itemType;
        maxItemStackSize = 32;
    }

    public int getMaxItemStackSize() {
        return maxItemStackSize;
    }

    public boolean isStackable() {
        return stackable;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
