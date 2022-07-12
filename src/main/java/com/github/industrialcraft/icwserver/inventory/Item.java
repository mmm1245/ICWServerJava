package com.github.industrialcraft.icwserver.inventory;

import com.github.industrialcraft.icwserver.net.messages.PlayerAttackMessage;
import com.github.industrialcraft.icwserver.util.State2AssetStorage;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.IItem;
import com.github.industrialcraft.inventorysystem.ItemData;
import com.github.industrialcraft.inventorysystem.ItemStack;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class Item implements IItem {
    private int stackSize;
    private String identifier;

    private ScriptObjectMirror attackHandler;
    private ScriptObjectMirror animationStateProvider;
    public final State2AssetStorage state2AssetStorage;
    public Item(int stackSize, String identifier, ScriptObjectMirror attackHandler, ScriptObjectMirror animationStateProvider, State2AssetStorage state2AssetStorage) {
        this.stackSize = stackSize;
        this.identifier = identifier;
        this.attackHandler = attackHandler;
        this.animationStateProvider = animationStateProvider;
        this.state2AssetStorage = state2AssetStorage;
    }
    public boolean onAttackHandlerCall(PlayerEntity player, ItemStack stack, PlayerAttackMessage message){
        if(attackHandler != null) {
            attackHandler.call(this, player, stack, message);
            return true;
        }
        return false;
    }
    public ScriptObjectMirror animationStateProvider() {
        return animationStateProvider;
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

    @Override
    public ItemData createData(ItemStack is) {
        return new JSItemData(is);
    }
    public static class JSItemData extends ItemData{
        Object data;
        public JSItemData(ItemStack is) {
            super(is);
            this.data = null;
        }
    }
}
