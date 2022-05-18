package com.github.industrialcraft.icwserver.world.entity.types;

import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.world.entity.Entity;

public class PlayerEntityData extends EntityData{
    public ClientConnection clientConnection;
    public String name;
    public PlayerEntityData(Entity entity) {
        super(entity);
    }
}
