package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.IDamagable;
import com.google.gson.JsonObject;

public abstract class DamageableEntity extends Entity implements IDamagable {
    float health;
    public DamageableEntity(Location location) {
        super(location);
        this.health = getMaxHealth();
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("health", health);
        return json;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }
    @Override
    public float getHealth() {
        return this.health;
    }
}
