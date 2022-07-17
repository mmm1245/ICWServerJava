package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class KnockbackDataMessage extends Message {
    public static final String TYPE = "knockbackData";

    public final float x;
    public final float y;
    public KnockbackDataMessage(float x, float y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("x", x);
        json.addProperty("y", y);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
