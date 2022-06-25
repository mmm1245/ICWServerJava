package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class PlayerClickSlotMessage extends Message {
    public static final String TYPE = "clickSlot";

    public final int slot;
    public final boolean secondInventory;
    public PlayerClickSlotMessage(JsonObject json) {
        this.slot = getOrException(json, "slot").getAsInt();
        this.secondInventory = getOrException(json, "secondInventory").getAsBoolean();
    }

    @Override
    public String toString() {
        return "PlayerClickSlotMessage{" +
                "slot=" + slot +
                ", secondInventory=" + secondInventory +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
