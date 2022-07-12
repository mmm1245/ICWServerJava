package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.physics.PhysicsObjectDataHolder;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJS;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJSPhysical;
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
    public final PhysicsObjectDataHolder physicsData;
    public JSEntityData(String type, float maxHealth, ScriptObjectMirror spawnMethod, ScriptObjectMirror tickMethod, ScriptObjectMirror onDeathMethod, ScriptObjectMirror onPlayerInteractMethod, ScriptObjectMirror damageTypeModifierMethod, ScriptObjectMirror animationStateProvider, PhysicsObjectDataHolder physicsData) {
        this.type = type;
        this.maxHealth = maxHealth;
        this.spawnMethod = spawnMethod;
        this.tickMethod = tickMethod;
        this.onDeathMethod = onDeathMethod;
        this.onPlayerInteractMethod = onPlayerInteractMethod;
        this.damageTypeModifierMethod = damageTypeModifierMethod;
        this.animationStateProvider = animationStateProvider;
        this.physicsData = physicsData;
    }
    public JSEntityData(String type, float maxHealth, ScriptObjectMirror spawnMethod, ScriptObjectMirror tickMethod, ScriptObjectMirror onDeathMethod, ScriptObjectMirror onPlayerInteractMethod, ScriptObjectMirror damageTypeModifierMethod, ScriptObjectMirror animationStateProvider) {
        this.type = type;
        this.maxHealth = maxHealth;
        this.spawnMethod = spawnMethod;
        this.tickMethod = tickMethod;
        this.onDeathMethod = onDeathMethod;
        this.onPlayerInteractMethod = onPlayerInteractMethod;
        this.damageTypeModifierMethod = damageTypeModifierMethod;
        this.animationStateProvider = animationStateProvider;
        this.physicsData = null;
    }

    public void spawn(JSLocation location){
        if(physicsData != null)
            new EntityFromJSPhysical(this, location.getInternal());
        else
            new EntityFromJS(this, location.getInternal());
    }
}
