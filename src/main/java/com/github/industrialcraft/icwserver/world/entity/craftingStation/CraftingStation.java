package com.github.industrialcraft.icwserver.world.entity.craftingStation;

import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;

public abstract class CraftingStation extends Entity {
    protected PhysicsObject physicsObject;
    protected Inventory inventory;
    public CraftingStation(Location location, PhysicsObjectDataHolder physicsHolder, int size) {
        super(location);
        this.physicsObject = physicsHolder.create(this);
        this.inventory = new Inventory(size, (inventory1, is) -> new ItemStackEntity(getLocation(), is), this);
    }

    @Override
    public void tick() {}

    @Override
    public PhysicsObject getPhysicalObject() {
        return physicsObject;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    @Override
    public boolean onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        player.openInventory(this);
        return true;
    }
}
