package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.data.InventoryCreationData;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.State2AssetStorage;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSStatusEffectRegistry {
    private HashMap<String, JSStatusEffectData> statusEffects;
    public JSStatusEffectRegistry() {
        this.statusEffects = new HashMap<>();
    }
    public StatusEffectTemplate createTemplate(String id){
        if(statusEffects.containsKey(id))
            throw new IllegalStateException(id + " already registered");
        return new JSStatusEffectRegistry.StatusEffectTemplate(id);
    }
    public Map<String,JSStatusEffectData> getStatusEffects(){
        return Collections.unmodifiableMap(statusEffects);
    }

    public class StatusEffectTemplate{
        private String id;
        private ScriptObjectMirror spawnMethod;
        private ScriptObjectMirror tickMethod;
        private boolean showToPlayer;
        public StatusEffectTemplate(String id) {
            this.id = id;
            this.showToPlayer = false;
        }
        public StatusEffectTemplate withOnTick(ScriptObjectMirror method){
            this.tickMethod = method;
            return this;
        }
        public StatusEffectTemplate withOnSpawn(ScriptObjectMirror method){
            this.spawnMethod = method;
            return this;
        }
        public StatusEffectTemplate withShowToPlayer(){
            this.showToPlayer = true;
            return this;
        }
        public JSStatusEffectData register(){
            if(statusEffects.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            JSStatusEffectData statusEffectData = new JSStatusEffectData(id, spawnMethod, tickMethod, showToPlayer);
            statusEffects.put(id, statusEffectData);
            return statusEffectData;
        }
    }
}
