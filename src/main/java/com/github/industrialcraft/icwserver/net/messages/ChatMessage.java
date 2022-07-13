package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ChatMessage extends Message {
    public static final String TYPE = "chatMessage";

    public final String text;
    public ChatMessage(String text) {
        this.text = text;
    }
    public ChatMessage(JsonObject json){
        this.text = getOrException(json, "text").getAsString();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("text", text);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
