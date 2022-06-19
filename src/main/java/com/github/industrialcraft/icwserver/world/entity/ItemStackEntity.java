package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.inventory.ItemSerializer;
import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.IOnPlayerInteract;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class ItemStackEntity extends DamageableEntity implements IPhysicalEntity, IOnPlayerInteract {
    private ItemStack is;
    private PhysicsObject physicsObject;
    public ItemStackEntity(Location location, ItemStack is) {
        super(location);
        this.is = is;
        this.physicsObject = new PhysicsObject(this, 4, 4, EPhysicsLayer.OBJECT);
    }

    @Override
    public void tick() {
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
    public void onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        player.getInventory().addItem(is.clone());
        kill();
    }
}
