package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.inventory.data.IPlayerAttackHandler;
import com.github.industrialcraft.icwserver.net.messages.PlayerAttackMessage;
import com.github.industrialcraft.icwserver.util.IJsonSerializable;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.IItem;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class Item implements IItem {
    private int stackSize;
    private String identifier;

    private IPlayerAttackHandler attackHandler;
    public Item(int stackSize, String identifier, IPlayerAttackHandler attackHandler) {
        this.stackSize = stackSize;
        this.identifier = identifier;
        this.attackHandler = attackHandler;
    }
    public boolean onAttackHandlerCall(PlayerEntity player, ItemStack stack, PlayerAttackMessage message){
        if(attackHandler != null) {
            attackHandler.onAttack(player, stack, message);
            return true;
        }
        return false;
    }
    public ItemStack createStack(){
        return createStack(1);
    }
    public ItemStack createStack(int count){
        return new ItemStack(this, count);
    }
    public String getIdentifier() {
        return identifier;
    }
    @Override
    public int getStackSize() {
        return stackSize;
    }
}
