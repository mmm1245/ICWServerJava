package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.script.*;
import com.github.industrialcraft.icwserver.util.taunt.Taunt;
import com.google.gson.JsonObject;

public class AssetDataMessage extends Message {
    public static final String TYPE = "assetData";

    public final JSEntityRegistry entityRegistry;
    public final JSItemRegistry itemRegistry;
    public final JSTauntRegistry tauntRegistry;
    public final JSSoundEffectRegistry soundEffectRegistry;
    public AssetDataMessage(JSEntityRegistry entityRegistry, JSItemRegistry itemRegistry, JSTauntRegistry tauntRegistry, JSSoundEffectRegistry soundEffectRegistry) {
        this.entityRegistry = entityRegistry;
        this.itemRegistry = itemRegistry;
        this.tauntRegistry = tauntRegistry;
        this.soundEffectRegistry = soundEffectRegistry;
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

        JsonObject jsonPlayerStates = new JsonObject();
        for(Taunt taunt : tauntRegistry.getTaunts().values()){
            for(int i = 0;i < taunt.parts.size();i++){
                jsonPlayerStates.addProperty(taunt.id + "_" + i, taunt.parts.get(i).asset());
            }
        }
        jsonPlayerStates.addProperty("default", "default_player.png");
        json.add("playerStates", jsonPlayerStates);

        JsonObject soundEffects = new JsonObject();
        for(JSSoundEffectRegistry.SoundEffect soundEffect : soundEffectRegistry.getSoundEffects().values()){
            soundEffects.addProperty(soundEffect.name, soundEffect.asset);
        }
        json.add("soundEffects", soundEffects);
        return json;
    }
    @Override
    public String getType() {
        return TYPE;
    }
}
