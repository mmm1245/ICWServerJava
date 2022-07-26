package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.script.JSStatusEffectData;

public class StatusEffect {
    public final JSStatusEffectData type;
    public int timeLeft;
    public Object data;
    public StatusEffect(JSStatusEffectData type, int timeLeft) {
        this.type = type;
        this.timeLeft = timeLeft;
        if(type.spawnMethod() != null)
            type.spawnMethod().call(this);
    }
    public void tick(Entity entity){
        if(type.tickMethod() != null)
            type.tickMethod().call(this, entity);//todo: JSEntity
        timeLeft--;
    }
}
