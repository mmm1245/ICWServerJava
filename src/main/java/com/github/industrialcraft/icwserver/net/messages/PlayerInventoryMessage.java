package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.inventory.ItemSerializer;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.entity.StatusEffect;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class PlayerInventoryMessage extends Message {
    public static final String TYPE = "playerInventory";

    private Inventory inventory;
    private Inventory secondInventory;
    private ItemStack handItem;
    private float health;
    private List<StatusEffect> statusEffects;
    public PlayerInventoryMessage(Inventory inventory, Inventory secondInventory, ItemStack handItem, float health, List<StatusEffect> statusEffects) {
        this.inventory = inventory;
        this.secondInventory = secondInventory;
        this.handItem = handItem;
        this.health = health;
        this.statusEffects = statusEffects;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("items", serializeInventory(inventory));
        json.add("itemsSecond", serializeInventory(secondInventory));
        json.add("handItem", ItemSerializer.toJson(handItem));
        json.addProperty("health", this.health);

        JsonArray jsonStatusEffects = new JsonArray();
        for(StatusEffect statusEffect : statusEffects){
            jsonStatusEffects.add(statusEffect.type.type());
        }
        json.add("statusEffects", jsonStatusEffects);
        return json;
    }

    private static JsonArray serializeInventory(Inventory inventory){
        if(inventory == null)
            return null;
        JsonArray items = new JsonArray();
        for(int i = 0;i < inventory.getSize();i++){
            ItemStack is = inventory.getAt(i);
            if(is == null)
                items.add((String) null);
            else
                items.add(ItemSerializer.toJson(is));
        }
        return items;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
