package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.Particle;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MapDataMessage extends Message {
    public static final String TYPE = "mapData";

    World world;
    public MapDataMessage(World world) {
        this.world = world;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray entitiesJson = new JsonArray();
        for(Entity entity : world.getEntities()){
            entitiesJson.add(entity.toJson());
        }
        json.add("entities", entitiesJson);

        JsonArray particleJson = new JsonArray();
        for(Particle particle : world.getParticles()){
            particleJson.add(particle.toJson());
        }
        json.add("particles", particleJson);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
