package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public record PhysicsObjectDataHolder(int hitboxW, int hitboxH, EPhysicsLayer layer) {
    public PhysicsObject create(Entity entity) {
        return new PhysicsObject(entity, hitboxW, hitboxH, layer);
    }
}
