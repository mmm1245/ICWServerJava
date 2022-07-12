package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class ItemSerializer {
    public static JsonObject toJson(ItemStack is){
        if(is == null)
            return null;
        JsonObject json = new JsonObject();
        json.addProperty("id",((Item)is.getItem()).getIdentifier());
        json.addProperty("count", is.getCount());
        ScriptObjectMirror animationProvider = ((Item) is.getItem()).animationStateProvider();
        if(animationProvider != null)
            json.addProperty("state", animationProvider.call(is).toString());
        return json;
    }
}
