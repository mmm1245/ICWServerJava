package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public class PhysicsObjectDataHolder {
    public final int hitboxW;
    public final int hitboxH;
    public final EPhysicsLayer layer;
    public PhysicsObjectDataHolder(int hitboxW, int hitboxH, EPhysicsLayer layer) {
        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;
        this.layer = layer;
    }
    public PhysicsObject create(Entity entity){
        return new PhysicsObject(entity, hitboxW, hitboxH, layer);
    }
}
