package com.github.industrialcraft.icwserver.world.entity.types;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public class EntityType {
    private final String type;
    private final int hitboxW;
    private final int hitboxH;
    public EntityType(String type, int hitboxW, int hitboxH) {
        this.type = type;
        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;
    }

    public void tick(Entity entity) {

    }
    public EntityData createData(Entity entity) {
        return new EntityData(entity);
    }
}
