package com.github.industrialcraft.icwserver.world.entity.craftingStation;

import com.github.industrialcraft.icwserver.inventory.Items;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;

public class WoodWorkingStation extends CraftingStation{
    public WoodWorkingStation(Location location) {
        super(location, new PhysicsObjectDataHolder(30, 10, EPhysicsLayer.OBJECT), 2);
        getInventory().addItem(new ItemStack(Items.LOG, 5));
        getInventory().addItem(new ItemStack(Items.PLANK, 10));
    }

    @Override
    public String getType() {
        return "woodWorkingStation";
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }
}
