package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.inventory.data.IActionProcessingInventory;
import com.github.industrialcraft.icwserver.inventory.data.IPlayerAttackHandler;
import com.github.industrialcraft.icwserver.world.entity.data.IPlayerInteractHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSItemRegistry {
    private HashMap<String, Item> items;
    public JSItemRegistry() {
        this.items = new HashMap<>();
    }
    public ItemTemplate createTemplate(String id, int stackSize){
        if(items.containsKey(id))
            throw new IllegalStateException(id + " already registered");
        return new ItemTemplate(id, stackSize);
    }
    public Map<String,Item> getItems(){
        return Collections.unmodifiableMap(items);
    }

    public class ItemTemplate{
        private String id;
        private int stackSize;
        private IPlayerAttackHandler attackHandler;
        public ItemTemplate(String id, int stackSize) {
            this.id = id;
            this.stackSize = stackSize;
        }
        public ItemTemplate withAttackHandler(IPlayerAttackHandler attackHandler){
            this.attackHandler = attackHandler;
            return this;
        }

        public Item register(){
            if(items.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            Item item = new Item(stackSize, id, attackHandler);
            items.put(id, item);
            return item;
        }
    }
}
