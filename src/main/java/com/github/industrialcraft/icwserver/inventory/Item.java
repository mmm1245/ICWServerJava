package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.inventorysystem.IItem;
import com.google.gson.JsonObject;

public class Item implements IItem {
    private int stackSize;
    private String identifier;
    public Item(int stackSize, String identifier) {
        this.stackSize = stackSize;
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }
    @Override
    public int getStackSize() {
        return stackSize;
    }
}
