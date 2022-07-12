package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSEntityRegistry {
    private HashMap<String, JSEntityData> entities;
    public JSEntityRegistry() {
        this.entities = new HashMap<>();
    }
    public EntityTemplate createTemplate(String id, int maxHealth){
        if(entities.containsKey(id))
            throw new IllegalStateException(id + " already registered");
        return new EntityTemplate(id, maxHealth);
    }
    public Map<String,JSEntityData> getEntities(){
        return Collections.unmodifiableMap(entities);
    }

    public class EntityTemplate{
        private String id;
        private float maxHealth;
        private ScriptObjectMirror spawnMethod;
        private ScriptObjectMirror tickMethod;
        private ScriptObjectMirror onDeathMethod;
        private ScriptObjectMirror onPlayerInteractMethod;
        private ScriptObjectMirror damageTypeModifierMethod;
        private ScriptObjectMirror animationStateProvider;
        private PhysicsObjectDataHolder physicsData;
        public EntityTemplate(String id, int maxHealth) {
            this.id = id;
            this.maxHealth = maxHealth;
        }
        public EntityTemplate withOnSpawn(ScriptObjectMirror method){
            this.spawnMethod = method;
            return this;
        }
        public EntityTemplate withOnTick(ScriptObjectMirror method){
            this.tickMethod = method;
            return this;
        }
        public EntityTemplate withDeath(ScriptObjectMirror method){
            this.onDeathMethod = method;
            return this;
        }
        public EntityTemplate withOnPlayerInteract(ScriptObjectMirror method){
            this.onPlayerInteractMethod = method;
            return this;
        }
        public EntityTemplate withDamageTypeModifier(ScriptObjectMirror method){
            this.damageTypeModifierMethod = method;
            return this;
        }
        public EntityTemplate withAnimationStateProvider(ScriptObjectMirror method){
            this.animationStateProvider = method;
            return this;
        }
        public EntityTemplate withPhysicsData(int width, int height, EPhysicsLayer physicsLayer){
            this.physicsData = new PhysicsObjectDataHolder(width, height, physicsLayer);
            return this;
        }

        public JSEntityData register(){
            if(entities.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            JSEntityData entityData = new JSEntityData(id, maxHealth, spawnMethod, tickMethod, onDeathMethod, onPlayerInteractMethod, damageTypeModifierMethod, animationStateProvider, physicsData);
            entities.put(id, entityData);
            return entityData;
        }
    }
}
