package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.StatusEffect;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJS;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;
import mikera.vectorz.Vector2;

import java.util.Collections;
import java.util.List;

public class JSEntity {
    private Entity entity;
    public JSEntity(Entity entity) {
        this.entity = entity;
    }
    public StatusEffect addStatusEffect(JSStatusEffectData type, int timeLeft){
        return this.entity.addStatusEffect(type, timeLeft);
    }
    public List<StatusEffect> getAllStatusEffects() {
        return this.entity.getAllStatusEffects();
    }
    public JSLocation location(){
        return new JSLocation(this.entity.getLocation());
    }
    public void teleport(Location location){
        this.entity.teleport(location);
    }
    public void teleport(float x, float y){
        this.entity.teleport(x, y);
    }
    public void applyKnockback(float x, float y){
        this.entity.applyKnockback(x, y);
    }
    public void applyKnockbackAtAngle(float angle, float magnitude){
        this.entity.applyKnockbackAtAngle(angle, magnitude);
    }
    public void setHealth(float health) {
        this.entity.setHealth(health);
    }
    public float getHealth() {
        return this.entity.getHealth();
    }
    public float getMaxHealth(){
        return this.entity.getMaxHealth();
    }
    public void damage(float damage, EDamageType type){
        this.entity.damage(damage, type);
    }
    public Inventory getInventory(){
        return this.entity.getInventory();
    }
    public boolean isDead() {
        return this.entity.isDead();
    }
    public void kill() {
        this.entity.kill();
    }
    public JSEntity clone(JSLocation newLocation){
        return new JSEntity(this.entity.clone(newLocation.getInternal()));
    }
    public PhysicsObject getPhysicalObject() {
        return this.entity.getPhysicalObject();
    }
    public void setData(Object data){
        this.entity.data = data;
    }
    public Object getData(){
        return this.entity.data;
    }
    public String getType(){
        return this.entity.getType();
    }
    public int getId(){
        return entity.id;
    }

    public JSPlayer toPlayerIP(){
        if(entity instanceof PlayerEntity player){
            return new JSPlayer(player);
        }
        return null;
    }
    public ItemStack toStackIP(){
        if(entity instanceof ItemStackEntity itemStackEntity){
            return itemStackEntity.getStack();
        }
        return null;
    }
    public JSEntityData toEntityDataIP(){
        if(entity instanceof EntityFromJS entityFromJS){
            return entityFromJS.getEntityData();
        }
        return null;
    }

    public Entity getInternal(){
        return this.entity;
    }
}
