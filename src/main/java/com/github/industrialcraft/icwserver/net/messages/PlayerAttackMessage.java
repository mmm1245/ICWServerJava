package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class PlayerAttackMessage extends Message {
    public static final String TYPE = "playerAttack";

    public final float angle;
    public PlayerAttackMessage(JsonObject json) {
        this.angle = getOrException(json, "angle").getAsFloat();
    }

    @Override
    public String toString() {
        return "PlayerAttackMessage{" +
                "angle=" + angle +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
