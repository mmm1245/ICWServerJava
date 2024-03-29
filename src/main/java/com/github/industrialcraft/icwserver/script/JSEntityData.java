package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.data.InventoryCreationData;
import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.util.PassengerData;
import com.github.industrialcraft.icwserver.util.State2AssetStorage;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJS;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class JSEntityData {
    public final String type;
    public final float maxHealth;
    public final ScriptObjectMirror spawnMethod;
    public final ScriptObjectMirror tickMethod;
    public final ScriptObjectMirror onDeathMethod;
    public final ScriptObjectMirror onPlayerInteractMethod;
    public final ScriptObjectMirror damageTypeModifierMethod;
    public final ScriptObjectMirror animationStateProvider;
    public final ScriptObjectMirror damageMethod;
    public final PhysicsObjectDataHolder physicsData;
    public final State2AssetStorage state2AssetStorage;
    public final InventoryCreationData inventoryCreationData;
    public final PassengerData passengerData;
    public JSEntityData(String type, float maxHealth, ScriptObjectMirror spawnMethod, ScriptObjectMirror tickMethod, ScriptObjectMirror onDeathMethod, ScriptObjectMirror onPlayerInteractMethod, ScriptObjectMirror damageTypeModifierMethod, ScriptObjectMirror animationStateProvider, ScriptObjectMirror damageMethod, PhysicsObjectDataHolder physicsData, State2AssetStorage state2AssetStorage, InventoryCreationData inventoryCreationData, PassengerData passengerData) {
        this.type = type;
        this.maxHealth = maxHealth;
        this.spawnMethod = spawnMethod;
        this.tickMethod = tickMethod;
        this.onDeathMethod = onDeathMethod;
        this.onPlayerInteractMethod = onPlayerInteractMethod;
        this.damageTypeModifierMethod = damageTypeModifierMethod;
        this.animationStateProvider = animationStateProvider;
        this.damageMethod = damageMethod;
        this.physicsData = physicsData;
        this.state2AssetStorage = state2AssetStorage;
        this.inventoryCreationData = inventoryCreationData;
        this.passengerData = passengerData;
    }

    public EntityFromJS spawn(JSLocation location, Object data){
        return new EntityFromJS(this, location.getInternal(), data);
    }
}
