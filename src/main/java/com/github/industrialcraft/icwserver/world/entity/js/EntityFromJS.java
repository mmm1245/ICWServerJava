package com.github.industrialcraft.icwserver.world.entity.js;

import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.script.JSEntity;
import com.github.industrialcraft.icwserver.script.JSEntityData;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.google.gson.JsonObject;

import javax.script.Invocable;

public class EntityFromJS extends Entity {
    private JSEntityData entityData;
    private PhysicsObject physicsObject;
    private Inventory inventory;
    public EntityFromJS(JSEntityData entitiyData, Location location, Object data) {
        super(location);
        this.entityData = entitiyData;
        if(entitiyData.physicsData!=null)
            this.physicsObject = entitiyData.physicsData.create(this);
        this.setHealth(getMaxHealth());
        this.inventory = entitiyData.inventoryCreationData==null?null:entitiyData.inventoryCreationData.create((inventory1, is) -> {
            EntityFromJS ent = inventory1.getData();
            if(!ent.isDead()){
                new ItemStackEntity(ent.getLocation(), is);
            }
        }, this);
        this.data = data;
        if(entitiyData.spawnMethod != null)
            entitiyData.spawnMethod.call(new JSEntity(this));
    }

    public boolean modData(Object data){
        try {
            ((Invocable)getServer().getScriptingManager().getEngine()).invokeFunction("mergeEntityData", this, data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(physicsObject != null) {
            physicsObject.tickKnockback();
        }
        if(entityData.tickMethod != null)
            entityData.tickMethod.call(new JSEntity(this));
    }
    @Override
    public void onDeath() {
        if(entityData.onDeathMethod != null)
            entityData.onDeathMethod.call(new JSEntity(this));
        if(inventory != null)
            inventory.dropAll();
    }
    @Override
    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        if(entityData.onPlayerInteractMethod != null){
            entityData.onPlayerInteractMethod.call(new JSEntity(this), player, message);
            return true;
        }
        return false;
    }

    @Override
    public Entity clone(Location newLocation) {
        EntityFromJS entity = new EntityFromJS(this.entityData, newLocation, getServer().getScriptingManager().tryDeepCopy(this.data));
        entity.setHealth(getHealth());
        entity.physicsObject = this.physicsObject.clone(entity);
        entity.dead = this.dead;
        //todo: copy inventory
        return entity;
    }

    @Override
    public float getDamageTypeModifier(EDamageType type) {
        if(entityData.damageTypeModifierMethod != null){
            return Float.parseFloat(entityData.damageTypeModifierMethod.call(new JSEntity(this), type).toString());
        }
        return 1;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        if(entityData.animationStateProvider != null)
            json.addProperty("state", entityData.animationStateProvider.call(new JSEntity(this)).toString());
        return json;
    }
    @Override
    public String getType() {
        return entityData.type;
    }
    public JSEntityData getEntityData() {
        return entityData;
    }
    @Override
    public float getMaxHealth() {
        return entityData ==null?1: entityData.maxHealth;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return this.physicsObject;
    }
}
