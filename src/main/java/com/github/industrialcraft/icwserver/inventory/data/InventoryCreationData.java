package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemOverflowHandler;
import org.openjdk.nashorn.internal.runtime.PropertyMap;
import org.openjdk.nashorn.internal.runtime.ScriptObject;

public record InventoryCreationData(int size) {
    public Inventory create(ItemOverflowHandler handler, Object data){
        return new Inventory(size, handler, data);
    }
}
