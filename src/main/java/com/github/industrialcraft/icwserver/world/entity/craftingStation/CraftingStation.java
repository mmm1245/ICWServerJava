package com.github.industrialcraft.icwserver.world.entity.craftingStation;

import com.github.industrialcraft.icwserver.inventory.data.IInventoryHolder;
import com.github.industrialcraft.icwserver.net.messages.InteractEntityMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.DamageableEntity;
import com.github.industrialcraft.icwserver.world.entity.IPhysicalEntity;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.data.IPlayerInteractHandler;
import com.github.industrialcraft.inventorysystem.Inventory;

public abstract class CraftingStation extends DamageableEntity implements IPhysicalEntity, IPlayerInteractHandler, IInventoryHolder {
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
    public boolean isInvalidForPlayer(PlayerEntity player) {
        return isDead() || player.getLocation().distanceToNS(getLocation()) > 50*50;
    }

    @Override
    public void onPlayerInteract(PlayerEntity player, InteractEntityMessage message) {
        player.openInventory(this);
    }
}
