package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class PlayerDismountMessage extends Message {
    public static final String TYPE = "playerDismount";

    public PlayerDismountMessage(JsonObject json) {}

    @Override
    public String getType() {
        return TYPE;
    }
}
