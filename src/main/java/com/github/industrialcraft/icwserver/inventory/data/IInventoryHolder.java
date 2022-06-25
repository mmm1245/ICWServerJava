package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;

public interface IInventoryHolder {
    Inventory getInventory();
    boolean isInvalidForPlayer(PlayerEntity player);
}
