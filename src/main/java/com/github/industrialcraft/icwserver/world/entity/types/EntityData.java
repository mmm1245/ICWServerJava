package com.github.industrialcraft.icwserver.world.entity.types;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public class EntityData {
    private final Entity entity;
    public EntityData(Entity entity) {
        this.entity = entity;
    }
    public Entity getEntity() {
        return entity;
    }
}
