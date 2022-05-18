package com.github.industrialcraft.icwserver.world;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.types.PlayerEntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class World {
    private boolean removed;
    private final boolean lobby;
    private final ArrayList<Entity> entities;
    private final GameServer server;
    private final int id;
    public World(boolean lobby, GameServer server) {
        this.id = server.generateIDWorld();
        this.server = server;
        this.removed = false;
        this.lobby = lobby;
        this.entities = new ArrayList<>();
    }
    public void tick(){
        entities.removeIf(entity -> entity.isRemoved()||entity.getLocation().world()!=this);
        for(Entity entity : this.entities){
            entity.tick();
        }
    }
    public void addEntity(Entity entity){
        this.entities.add(entity);
    }

    public Location getSpawn(){
        return new Location(0, 0, this);
    }
    public void remove(){
        if(lobby)
            return;
        this.removed = true;
        Location spawn = this.server.getLobby().getSpawn();
        for(Entity entity : this.entities){
            if(entity.getType() instanceof PlayerEntityType){
                entity.teleport(spawn);
            }
        }
        this.entities.clear();
    }
    public List<Entity> getEntities(){
        return Collections.unmodifiableList(this.entities);
    }
    public boolean isRemoved(){
        return this.removed;
    }
    public GameServer getServer() {
        return server;
    }
    public int getId() {
        return id;
    }
}
