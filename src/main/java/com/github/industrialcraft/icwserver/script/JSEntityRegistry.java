package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.data.InventoryCreationData;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.PassengerData;
import com.github.industrialcraft.icwserver.util.State2AssetStorage;
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
        private ScriptObjectMirror damageMethod;
        private PhysicsObjectDataHolder physicsData;
        private State2AssetStorage state2AssetStorage;
        private InventoryCreationData inventoryCreationData;
        private PassengerData passengerData;
        public EntityTemplate(String id, int maxHealth) {
            this.id = id;
            this.maxHealth = maxHealth;
            this.state2AssetStorage = new State2AssetStorage();
        }
        public void addRenderState(String state, String asset){
            this.state2AssetStorage.addState(state, asset);
        }
        public EntityTemplate withOnSpawn(ScriptObjectMirror method){
            this.spawnMethod = method;
            return this;
        }
        public EntityTemplate withOnTick(ScriptObjectMirror method){
            this.tickMethod = method;
            return this;
        }
        public EntityTemplate withOnDeath(ScriptObjectMirror method){
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
        public EntityTemplate withOnDamage(ScriptObjectMirror method){
            this.damageMethod = method;
            return this;
        }
        public EntityTemplate withPhysicsData(int width, int height, EPhysicsLayer physicsLayer){
            this.physicsData = new PhysicsObjectDataHolder(width, height, physicsLayer);
            return this;
        }
        public EntityTemplate withInventory(int size){
            this.inventoryCreationData = new InventoryCreationData(size);
            return this;
        }
        public EntityTemplate withPassengerData(int offsetX, int offsetY){
            this.passengerData = new PassengerData(offsetX, offsetY);
            return this;
        }

        public JSEntityData register(){
            if(entities.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            JSEntityData entityData = new JSEntityData(id, maxHealth, spawnMethod, tickMethod, onDeathMethod, onPlayerInteractMethod, damageTypeModifierMethod, animationStateProvider, damageMethod, physicsData, state2AssetStorage, inventoryCreationData, passengerData);
            entities.put(id, entityData);
            return entityData;
        }
    }
}
