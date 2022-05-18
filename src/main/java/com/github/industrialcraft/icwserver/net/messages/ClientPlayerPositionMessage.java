package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class ClientPlayerPositionMessage extends Message {
    public static final String TYPE = "clientPlayerPosition";

    public final float x;
    public final float y;
    public ClientPlayerPositionMessage(JsonObject json) {
        this.x = json.get("x").getAsFloat();
        this.y = json.get("y").getAsFloat();
    }

    @Override
    public String toString() {
        return "ClientPlayerPositionMessage{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
