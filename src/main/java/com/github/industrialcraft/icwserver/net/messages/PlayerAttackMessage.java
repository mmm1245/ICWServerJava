package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class PlayerAttackMessage extends Message {
    public static final String TYPE = "playerAttack";

    public final float angle;
    public final float power;
    public PlayerAttackMessage(JsonObject json) {
        this.angle = getOrException(json, "angle").getAsFloat();
        this.power = getOrException(json, "power").getAsFloat();
    }

    @Override
    public String toString() {
        return "PlayerAttackMessage{" +
                "angle=" + angle +
                ", power=" + power +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
