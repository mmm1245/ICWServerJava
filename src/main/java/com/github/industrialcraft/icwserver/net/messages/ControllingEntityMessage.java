package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ControllingEntityMessage extends Message {
    public static final String TYPE = "controllingEntity";

    Entity controllingEntity;
    public ControllingEntityMessage(Entity controllingEntity) {
        this.controllingEntity = controllingEntity;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("controllingId", controllingEntity.id);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
