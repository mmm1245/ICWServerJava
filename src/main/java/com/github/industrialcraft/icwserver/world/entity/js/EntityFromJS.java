package com.github.industrialcraft.icwserver.world.entity.js;

import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.script.JSEntityData;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.google.gson.JsonObject;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptException;

public class EntityFromJS extends Entity {
    private JSEntityData entitiyData;
    private PhysicsObject physicsObject;
    private Inventory inventory;
    public EntityFromJS(JSEntityData entitiyData, Location location, Object data) {
        super(location);
        this.entitiyData = entitiyData;
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
            entitiyData.spawnMethod.call(this);
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
        if(physicsObject != null) {
            physicsObject.tickKnockback();
        }
        if(entitiyData.tickMethod != null)
            entitiyData.tickMethod.call(this);
    }
    @Override
    public void onDeath() {
        if(entitiyData.onDeathMethod != null)
            entitiyData.onDeathMethod.call(this);
        if(inventory != null)
            inventory.dropAll();
    }
    @Override
    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        if(entitiyData.onPlayerInteractMethod != null){
            entitiyData.onPlayerInteractMethod.call(this, player, message);
            return true;
        }
        return false;
    }

    @Override
    public Entity clone(Location newLocation) {
        EntityFromJS entity = new EntityFromJS(this.entitiyData, newLocation, getServer().getScriptingManager().tryDeepCopy(this.data));
        entity.setHealth(getHealth());
        entity.physicsObject = this.physicsObject.clone(entity);
        entity.dead = this.dead;
        entity.frozen = this.frozen;
        //todo: copy inventory
        return entity;
    }

    @Override
    public float getDamageTypeModifier(EDamageType type) {
        if(entitiyData.damageTypeModifierMethod != null){
            return Float.parseFloat(entitiyData.damageTypeModifierMethod.call(this, type).toString());
        }
        return 1;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        if(entitiyData.animationStateProvider != null)
            json.addProperty("state", entitiyData.animationStateProvider.call(this).toString());
        return json;
    }
    @Override
    public String getType() {
        return entitiyData.type;
    }
    @Override
    public float getMaxHealth() {
        return entitiyData==null?1:entitiyData.maxHealth;
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
