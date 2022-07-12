package com.github.industrialcraft.icwserver.world;

import com.github.industrialcraft.icwserver.util.Location;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Particle {
    float x;
    float y;
    int lifetime;
    String type;
    JsonObject data;
    public Particle(String type, int lifetime, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.lifetime = lifetime;
        this.data = new JsonObject();
    }
    public Particle(String type, int lifetime, Location location) {
        this.type = type;
        this.x = location.x();
        this.y = location.y();
        this.lifetime = lifetime;
        this.data = new JsonObject();
    }
    public Particle addNumber(String property, Number value){
        data.addProperty(property, value);
        return this;
    }
    public Particle addString(String property, String value){
        data.addProperty(property, value);
        return this;
    }
    public Particle addJson(String property, JsonElement value){
        data.add(property, value);
        return this;
    }
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("x", x);
        json.addProperty("y", y);
        json.addProperty("type", type);
        json.add("data", data);
        return json;
    }
    public boolean decreaseLifetime(int by){
        lifetime -= by;
        return (lifetime <= 0);
    }
    public void setLifetime(int lifetime){
        if(lifetime > 0)
            this.lifetime = lifetime;
    }
    public int getLifetime() {
        return lifetime;
    }
    public void remove(){
        this.lifetime = 0;
    }
}
