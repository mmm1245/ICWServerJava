package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.google.gson.JsonObject;

public class PlatformEntity extends Entity {
    private PhysicsObject physicsObject;
    public PlatformEntity(Location location, int width, int height) {
        super(location);
        this.physicsObject = new PhysicsObject(this, width, height, EPhysicsLayer.WALL);
    }

    @Override
    public void tick() {}

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("width", physicsObject.getHitboxW());
        json.addProperty("height", physicsObject.getHitboxH());
        return json;
    }

    @Override
    public String getType() {
        return "platform";
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }
    @Override
    public float getDamageTypeModifier(EDamageType type) {
        return 0;
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return this.physicsObject;
    }
}
