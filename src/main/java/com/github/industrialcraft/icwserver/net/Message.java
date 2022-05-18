package com.github.industrialcraft.icwserver.net;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Message {
    public JsonObject toJson(){throw new UnsupportedOperationException("attempting to serialize non-serializable message");}
    public abstract String getType();

    protected static JsonElement getOrException(JsonObject json, String field){
        JsonElement element = json.get(field);
        if(element.isJsonNull())
            throw new RuntimeException("field "+field+" was null on " + json.get("type").getAsString());
        return element;
    }
}
