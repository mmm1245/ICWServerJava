package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.world.entity.types.EntityType;
import com.github.industrialcraft.icwserver.world.entity.types.PlayerEntityType;

public class Entities {
    public static final EntityType PLATFORM = new EntityType("platform", 100, 20);
    public static final EntityType PLAYER = new PlayerEntityType("player", 5, 20);
}
