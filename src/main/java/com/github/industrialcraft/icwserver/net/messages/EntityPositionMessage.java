package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonObject;

public class EntityPositionMessage extends Message {
    public static final String TYPE = "entityPosition";

    int id;
    int worldId;
    float x;
    float y;
    public EntityPositionMessage(Entity entity) {
        this.id = entity.getId();
        this.worldId = entity.getLocation().world().getId();
        this.x = entity.getLocation().x();
        this.y = entity.getLocation().y();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("world", this.worldId);
        json.addProperty("x", this.x);
        json.addProperty("y", this.y);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
