package com.github.industrialcraft.icwserver.particle;

import com.google.gson.JsonObject;

public abstract class Particle {
    float x;
    float y;
    int lifetime;
    public Particle(int lifetime, float x, float y) {
        this.x = x;
        this.y = y;
        this.lifetime = lifetime;
    }
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("x", x);
        json.addProperty("y", y);
        json.addProperty("type", getType());
        return json;
    }
    public boolean decreaseLifetime(){
        lifetime--;
        return (lifetime <= 0);
    }
    public int getLifetime() {
        return lifetime;
    }
    public void remove(){
        this.lifetime = 0;
    }

    public abstract String getType();
}
