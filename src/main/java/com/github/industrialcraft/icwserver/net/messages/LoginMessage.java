package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LoginMessage extends Message {
    public static final String TYPE = "login";

    public final String username;
    public LoginMessage(JsonObject json) {
        this.username = getOrException(json, "username").getAsString();
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
