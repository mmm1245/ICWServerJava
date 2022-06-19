package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.inventory.ItemSerializer;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PlayerInventoryMessage extends Message {
    public static final String TYPE = "playerInventory";

    private Inventory inventory;
    public PlayerInventoryMessage(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray items = new JsonArray();
        for(int i = 0;i < inventory.getSize();i++){
            ItemStack is = inventory.getAt(i);
            if(is == null)
                items.add((String) null);
            else
                items.add(ItemSerializer.toJson(is));
        }
        json.add("items", items);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
