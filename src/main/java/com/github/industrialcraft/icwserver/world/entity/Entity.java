package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.script.JSStatusEffectData;
import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.data.IKillable;
import com.github.industrialcraft.icwserver.world.entity.data.IPlayerInteractHandler;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.google.gson.JsonObject;
import mikera.vectorz.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Entity implements IKillable, IJsonSerializable {
    protected Location location;
    protected boolean dead;
    protected float health;
    public final int id;
    public Object data;
    private final ArrayList<StatusEffect> statusEffects;
    public Entity(Location location) {
        this.statusEffects = new ArrayList<>();
        this.location = location;
        this.dead = false;
        this.location.world().addEntity(this);
        this.health = getMaxHealth();
        this.id = location.world().getServer().generateIDEntity();
    }

    public GameServer getServer(){
        return location.world().getServer();
    }
    public StatusEffect addStatusEffect(JSStatusEffectData type, int timeLeft){
        StatusEffect statusEffect = new StatusEffect(type, timeLeft);
        this.statusEffects.add(statusEffect);
        return statusEffect;
    }
    public List<StatusEffect> getAllStatusEffects() {
        return Collections.unmodifiableList(statusEffects);
    }
    public void tick(){
        statusEffects.forEach(statusEffect -> statusEffect.tick(this));
        statusEffects.removeIf(statusEffect -> statusEffect.timeLeft <= 0);
    }

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

    public void applyKnockback(float x, float y){
        PhysicsObject po = getPhysicalObject();
        if(po != null){
            po.applyKnockback(x, y);
        }
    }
    public void applyKnockbackAtAngle(float angle, float magnitude){
        PhysicsObject po = getPhysicalObject();
        if(po != null){
            Vector2 vector = new Vector2((Math.cos(Math.toRadians((angle + 360) % 360))),(Math.sin(Math.toRadians((angle + 360) % 360))));
            vector.normalise();
            vector.multiply(magnitude);
            po.applyKnockback((float)vector.x, (float)vector.y);
        }
    }

    @Override
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("x", location.x());
        json.addProperty("y", location.y());
        json.addProperty("type", getType());
        json.addProperty("health", health);
        if(getPhysicalObject() != null){
            json.addProperty("width", getPhysicalObject().getHitboxW());
            json.addProperty("height", getPhysicalObject().getHitboxH());
            json.addProperty("physicsLayer", getPhysicalObject().getLayer().name());
        }
        return json;
    }

    public abstract String getType();

    public void onDeath(){}

    public void setHealth(float health) {
        this.health = health;
        if(health <= 0)
            kill();
    }
    public float getHealth() {
        return this.health;
    }
    public abstract float getMaxHealth();
    public float getDamageTypeModifier(EDamageType type){
        return 1f;
    }
    public void damage(float damage, EDamageType type){
        float healthNew = getHealth()+(damage*getDamageTypeModifier(type));
        if(healthNew <= 0) {
            healthNew = 0;
        }
        if(healthNew > getMaxHealth())
            healthNew = getMaxHealth();
        setHealth(healthNew);
    }

    public Inventory getInventory(){
        return null;
    }

    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message){
        return false;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
    @Override
    public void kill() {
        this.dead = true;
    }

    public abstract Entity clone(Location newLocation);

    public PhysicsObject getPhysicalObject() {
        return null;
    }
}
