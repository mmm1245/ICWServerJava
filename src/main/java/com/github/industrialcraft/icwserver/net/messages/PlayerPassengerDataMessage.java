package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.script.JSSoundEffectRegistry;
import com.google.gson.JsonObject;

public class PlayerPassengerDataMessage extends Message {
    public static final String TYPE = "playerPassengerData";

    public int riddenEntityId;
    public int offsetX;
    public int offsetY;
    public PlayerPassengerDataMessage(int riddenEntityId, int offsetX, int offsetY) {
        this.riddenEntityId = riddenEntityId;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("riddenEntityId", riddenEntityId);
        json.addProperty("offsetX", offsetX);
        json.addProperty("offsetY", offsetY);
        return json;
    }
    @Override
    public String getType() {
        return TYPE;
    }
}
