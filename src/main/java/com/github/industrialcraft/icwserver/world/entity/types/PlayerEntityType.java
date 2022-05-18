package com.github.industrialcraft.icwserver.world.entity.types;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public class PlayerEntityType extends EntityType{
    public PlayerEntityType(String type, int hitboxW, int hitboxH) {
        super(type, hitboxW, hitboxH);
    }
    @Override
    public EntityData createData(Entity entity) {
        return new PlayerEntityData(entity);
    }
}
