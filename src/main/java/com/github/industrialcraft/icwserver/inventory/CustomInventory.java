package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.inventory.data.IActionProcessingInventory;
import com.github.industrialcraft.icwserver.net.messages.OpenedInventoryActionMessage;
import com.github.industrialcraft.icwserver.script.JSPlayer;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemOverflowHandler;
import com.github.industrialcraft.inventorysystem.ItemStack;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class CustomInventory extends Inventory implements IActionProcessingInventory {
    private ScriptObjectMirror canPutMethod;
    private ScriptObjectMirror inventoryActionMethod;
    private boolean isModified;
    public CustomInventory(int size, ItemOverflowHandler handler, Object data, ScriptObjectMirror canPutMethod, ScriptObjectMirror inventoryActionMethod) {
        super(size, handler, data);
        this.canPutMethod = canPutMethod;
        this.inventoryActionMethod = inventoryActionMethod;
        this.isModified = true;
    }
    @Override
    public boolean canPut(int index, ItemStack is) {
        if(canPutMethod == null)
            return true;
        return canPutMethod.call(this, index, is).toString().equalsIgnoreCase("true");
    }
    @Override
    public void onInventoryAction(PlayerEntity player, Inventory inventory, OpenedInventoryActionMessage msg) {
        if(inventoryActionMethod != null)
            inventoryActionMethod.call(this, new JSPlayer(player), msg);
    }

    @Override
    public void setAt(int index, ItemStack itemStack) {
        super.setAt(index, itemStack);
        this.isModified = true;
    }
    public void cleanModified(){
        this.isModified = false;
    }
    public boolean isModified(){
        return this.isModified;
    }
}
