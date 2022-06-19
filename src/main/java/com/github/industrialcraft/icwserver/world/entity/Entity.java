package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.IKillable;
import com.google.gson.JsonObject;

public abstract class Entity implements IKillable, IJsonSerializable {
    protected Location location;
    protected boolean dead;
    public final int id;
    public Entity(Location location) {
        this.location = location;
        this.dead = false;
        this.location.world().addEntity(this);
        this.id = location.world().getServer().generateIDEntity();
    }

    public abstract void tick();

    public Location getLocation(){
        return this.location;
    }
    public void teleport(Location location){
        if(this.location.world() != location.world()){
            location.world().addEntity(this);
        }
        this.location = location;
    }
    public void teleport(float x, float y){
        teleport(this.location.withXY(x, y));
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("x", location.x());
        json.addProperty("y", location.y());
        json.addProperty("type", getType());
        return json;
    }

    public abstract String getType();

    @Override
    public boolean isDead() {
        return dead;
    }
    @Override
    public void kill() {
        this.dead = true;
    }
}
