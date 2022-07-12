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

public class EntityFromJS extends Entity {
    private JSEntityData data;
    private PhysicsObject physicsObject;
    private Inventory inventory;
    public EntityFromJS(JSEntityData data, Location location) {
        super(location);
        this.data = data;
        if(data.physicsData!=null)
            this.physicsObject = data.physicsData.create(this);
        this.setHealth(getMaxHealth());
        this.inventory = data.inventoryCreationData==null?null:data.inventoryCreationData.create((inventory1, is) -> {
            EntityFromJS ent = inventory1.getData();
            if(!ent.isDead()){
                new ItemStackEntity(ent.getLocation(), is);
            }
        }, this);
        if(data.spawnMethod != null)
            data.spawnMethod.call(this);
    }

    @Override
    public void tick() {
        if(data.tickMethod != null)
            data.tickMethod.call(this);
    }
    @Override
    public void onDeath() {
        if(data.onDeathMethod != null)
            data.onDeathMethod.call(this);
        if(inventory != null)
            inventory.dropAll();
    }
    @Override
    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        if(data.onPlayerInteractMethod != null){
            data.onPlayerInteractMethod.call(this, player, message);
            return true;
        }
        return false;
    }
    @Override
    public float getDamageTypeModifier(EDamageType type) {
        if(data.damageTypeModifierMethod != null){
            return ((Float)data.damageTypeModifierMethod.call(this, type));
        }
        return 1;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        if(data.animationStateProvider != null)
            json.addProperty("state", data.animationStateProvider.call(this).toString());
        return json;
    }
    @Override
    public String getType() {
        return data.type;
    }
    @Override
    public float getMaxHealth() {
        return data==null?1:data.maxHealth;
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
