package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonObject;

public class TeleportPlayerMessage extends Message {
    public static final String TYPE = "teleportPlayer";

    private Entity entity;
    public TeleportPlayerMessage(Entity entity) {
        this.entity = entity;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("x", entity.getLocation().x());
        json.addProperty("y", entity.getLocation().y());
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
