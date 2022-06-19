package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class ItemSerializer {
    public static JsonObject toJson(ItemStack is){
        JsonObject json = new JsonObject();
        json.addProperty("id",((Item)is.getItem()).getIdentifier());
        json.addProperty("count", is.getCount());
        if(is.getData() instanceof IJsonSerializable serializableData)
            json.add("data", serializableData.toJson());
        return json;
    }
}
