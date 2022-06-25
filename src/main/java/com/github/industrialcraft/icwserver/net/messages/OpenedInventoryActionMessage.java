package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class OpenedInventoryActionMessage extends Message {
    public static final String TYPE = "openedInventoryAction";

    public final int action;
    public OpenedInventoryActionMessage(JsonObject json) {
        this.action = getOrException(json, "action").getAsInt();
    }

    @Override
    public String toString() {
        return "OpenedInventoryActionMessage{" +
                "action=" + action +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
