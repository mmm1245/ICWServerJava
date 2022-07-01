package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.icwserver.net.messages.OpenedInventoryActionMessage;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;

public interface IActionProcessingInventory {
    void onInventoryAction(PlayerEntity player, Inventory inventory, OpenedInventoryActionMessage msg);
}
