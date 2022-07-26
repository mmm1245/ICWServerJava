package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.inventory.ItemSerializer;
import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.IPlayerInteractHandler;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class ItemStackEntity extends Entity {
    private ItemStack is;
    private PhysicsObject physicsObject;
    public ItemStackEntity(Location location, ItemStack is) {
        super(location);
        this.is = is.clone();
        this.physicsObject = new PhysicsObject(this, 4, 4, EPhysicsLayer.OBJECT);
    }

    @Override
    public void tick() {
        super.tick();
        this.physicsObject.tickKnockback();
        //todo:check world orientation
        this.physicsObject.moveBy(0, -1);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.add("item", ItemSerializer.toJson(is));
        return json;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return physicsObject;
    }

    @Override
    public float getMaxHealth() {
        return 10;
    }

    @Override
    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        if(!isDead()) {
            player.getInventory().addItem(is.clone());
            kill();
        }
        return true;
    }

    @Override
    public Entity clone(Location newLocation) {
        ItemStackEntity entity = new ItemStackEntity(newLocation, is.clone());
        entity.setHealth(getHealth());
        entity.physicsObject = this.physicsObject.clone(entity);
        entity.dead = this.dead;
        return entity;
    }
    public ItemStack getStack() {
        return is;
    }
}
