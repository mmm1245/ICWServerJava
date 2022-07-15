package com.github.industrialcraft.icwserver.world;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class World {
    protected boolean removed;
    protected final boolean lobby;
    protected final LinkedList<Entity> entitiesToAdd;
    protected final ArrayList<Entity> entities;
    protected final ArrayList<Particle> particles;
    protected final GameServer server;
    protected final int id;
    public World(boolean lobby, GameServer server) {
        this.id = server.generateIDWorld();
        this.server = server;
        this.removed = false;
        this.lobby = lobby;
        this.entitiesToAdd = new LinkedList<>();
        this.entities = new ArrayList<>();
        this.particles = new ArrayList<>();
    }
    public void tick(){
        partialTick();
        for(Entity entity : this.entities){
            if(!entity.frozen)
                entity.tick();
        }
    }
    public void partialTick(){
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        entities.forEach(entity -> {
            if(entity.isDead())
                entity.onDeath();
        });
        entities.removeIf(entity -> entity.isDead()||entity.getLocation().world()!=this);
        particles.removeIf(particle -> particle.decreaseLifetime(1));
    }
    public void addEntity(Entity entity){
        this.entitiesToAdd.add(entity);
    }
    public void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public Entity byId(int id){
        for(Entity entity : entities){
            if(entity.id == id)
                return entity;
        }
        return null;
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
            if(entity instanceof PlayerEntity)
                entity.teleport(spawn);
        }
        this.entities.clear();
    }
    public List<Entity> getEntities(){
        return Collections.unmodifiableList(this.entities);
    }
    public List<Particle> getParticles(){
        return Collections.unmodifiableList(this.particles);
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
