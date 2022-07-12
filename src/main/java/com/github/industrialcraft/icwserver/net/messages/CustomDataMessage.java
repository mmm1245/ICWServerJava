package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CustomDataMessage extends Message
{
    public static final String TYPE = "customData";

    public final String type;
    public final String data;
    public CustomDataMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }
    public CustomDataMessage(JsonObject json){
        this.type = getOrException(json, "type").getAsString();
        JsonElement dataElement = json.get("data");
        this.data = dataElement==null?null:dataElement.getAsString();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        if(data != null)
            json.addProperty("data", data);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
