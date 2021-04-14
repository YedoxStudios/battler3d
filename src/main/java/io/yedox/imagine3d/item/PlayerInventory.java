package io.yedox.imagine3d.item;

public class PlayerInventory {
    private ItemStack[] itemStacks;
    private int itemsAdded;

    public PlayerInventory() {
        itemStacks = new ItemStack[10];
        itemsAdded = 0;
    }

    public void addItemStack(ItemStack itemStack) throws InventoryOutOfSpaceException {
        if(itemsAdded < 10) {
            itemStacks[itemsAdded++] = itemStack;
        } else {
            throw new InventoryOutOfSpaceException("Out of inventory space");
        }
    }

    public ItemStack[] getItemStackList() {
        return itemStacks;
    }
}
