package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.script.JSEntityData;
import com.github.industrialcraft.icwserver.script.JSEntityRegistry;
import com.github.industrialcraft.icwserver.script.JSItemRegistry;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AssetDataMessage extends Message {
    public static final String TYPE = "assetData";

    public final JSEntityRegistry entityRegistry;
    public final JSItemRegistry itemRegistry;
    public AssetDataMessage(JSEntityRegistry entityRegistry, JSItemRegistry itemRegistry) {
        this.entityRegistry = entityRegistry;
        this.itemRegistry = itemRegistry;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonObject jsonEntities = new JsonObject();
        for(JSEntityData entity : entityRegistry.getEntities().values()){
            jsonEntities.add(entity.type, entity.state2AssetStorage.toJson());
        }
        json.add("entities", jsonEntities);
        JsonObject jsonItems = new JsonObject();
        for(Item item : itemRegistry.getItems().values()){
            jsonItems.add(item.getIdentifier(), item.state2AssetStorage.toJson());
        }
        json.add("items", jsonItems);
        return json;
    }
    @Override
    public String getType() {
        return TYPE;
    }
}
