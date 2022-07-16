package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonObject;

public class PlayTauntMessage extends Message {
    public static final String TYPE = "playTaunt";

    public final String taunt;
    public PlayTauntMessage(JsonObject json) {
        this.taunt = getOrException(json, "taunt").getAsString();
    }
    public boolean isStopMessage(){
        return this.taunt.isBlank();
    }

    @Override
    public String toString() {
        return "PlayTauntMessage{" +
                "taunt='" + taunt + '\'' +
                '}';
    }
    @Override
    public String getType() {
        return TYPE;
    }
}
