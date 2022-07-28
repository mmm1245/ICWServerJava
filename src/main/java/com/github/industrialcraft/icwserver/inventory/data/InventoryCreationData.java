package com.github.industrialcraft.icwserver.inventory.data;

import com.github.industrialcraft.icwserver.inventory.CustomInventory;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemOverflowHandler;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;
import org.openjdk.nashorn.internal.runtime.PropertyMap;
import org.openjdk.nashorn.internal.runtime.ScriptObject;

public record InventoryCreationData(int size, ScriptObjectMirror canPutMethod, ScriptObjectMirror inventoryActionMethod) {
    public Inventory create(ItemOverflowHandler handler, Object data){
        return new CustomInventory(size, handler, data, canPutMethod, inventoryActionMethod);
    }
}
