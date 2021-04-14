package io.yedox.imagine3d.item;

import java.util.ArrayList;

public class ItemStack {
    private ArrayList<Item> itemStack;
    private ItemType itemType;

    public ItemStack(ItemType itemType) {
        itemStack = new ArrayList<>();
        this.itemType = itemType;
    }

    public void addItem(Item item) {
        if (item.getItemType() == itemType)
            if (itemStack.size() < item.getMaxItemStackSize())
                itemStack.add(item);
    }

    public Item getItemAt(int index) {
        return itemStack.get(index);
    }

    public ArrayList<Item> getItemStack() {
        return itemStack;
    }

    public void setItemStack(ArrayList<Item> itemStack) {
        this.itemStack = itemStack;
    }
}
