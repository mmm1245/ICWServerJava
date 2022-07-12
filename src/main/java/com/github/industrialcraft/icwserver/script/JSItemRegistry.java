package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.util.State2AssetStorage;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

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
        private ScriptObjectMirror attackHandler;
        private ScriptObjectMirror animationStateProvider;
        private State2AssetStorage state2AssetStorage;
        public ItemTemplate(String id, int stackSize) {
            this.id = id;
            this.stackSize = stackSize;
            this.state2AssetStorage = new State2AssetStorage();
        }
        public void addRenderState(String state, String asset){
            this.state2AssetStorage.addState(state, asset);
        }
        public ItemTemplate withAttackHandler(ScriptObjectMirror attackHandler){
            this.attackHandler = attackHandler;
            return this;
        }
        public ItemTemplate withAnimationStateProvider(ScriptObjectMirror animationStateProvider){
            this.animationStateProvider = animationStateProvider;
            return this;
        }

        public Item register(){
            if(items.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            Item item = new Item(stackSize, id, attackHandler, animationStateProvider, state2AssetStorage);
            items.put(id, item);
            return item;
        }
    }
}
