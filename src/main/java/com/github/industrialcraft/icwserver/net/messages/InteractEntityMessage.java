package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class InteractEntityMessage extends Message {
    public static final String TYPE = "interactEntity";

    public final int entityId;
    public InteractEntityMessage(JsonObject json) {
        this.entityId = getOrException(json, "entityId").getAsInt();
    }

    @Override
    public String toString() {
        return "InteractEntityMessage{" +
                "entityId=" + entityId +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
