package com.github.industrialcraft.icwserver.particle;

import com.google.gson.JsonObject;

public class GunshotParticle extends Particle{
    private float angle;
    private float length;
    private float bulletAmt;
    public GunshotParticle(int lifetime, float x, float y, float angle, float length, float bulletAmt) {
        super(lifetime, x, y);
        this.angle = angle;
        this.length = length;
        this.bulletAmt = bulletAmt;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("angle", angle);
        json.addProperty("length", length);
        json.addProperty("bulletAmt", bulletAmt);
        return json;
    }

    @Override
    public String getType() {
        return "gunshot";
    }
}
