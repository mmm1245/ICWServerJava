package com.github.industrialcraft.icwserver.util;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class State2AssetStorage {
    HashMap<String,String> state2asset;
    public State2AssetStorage() {
        this.state2asset = new HashMap<>();
    }
    public void addState(String state, String asset){
        this.state2asset.put(state, asset);
    }
    public boolean hasDefault(){
        return this.state2asset.containsKey("default");
    }
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        for (Map.Entry<String,String> entries : state2asset.entrySet()){
            json.addProperty(entries.getKey(), entries.getValue());
        }
        return json;
    }
}
