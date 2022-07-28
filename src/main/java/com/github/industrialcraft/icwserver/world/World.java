package com.github.industrialcraft.icwserver.world;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.messages.SoundEffectMessage;
import com.github.industrialcraft.icwserver.script.JSSoundEffectRegistry;
import com.github.industrialcraft.icwserver.util.EWorldOrientation;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import mikera.vectorz.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class World {
    protected boolean removed;
    public final boolean lobby;
    protected final LinkedList<Entity> entitiesToAdd;
    protected final ArrayList<Entity> entities;
    protected final ArrayList<Particle> particles;
    protected final GameServer server;
    public EWorldOrientation orientation;
    protected final int id;
    public Object data;
    public World(boolean lobby, EWorldOrientation orientation, GameServer server) {
        this.id = server.generateIDWorld();
        this.orientation = orientation;
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
        particles.removeIf(particle -> particle.decreaseLifetime(1));//todo: move out of partial tick
    }
    public void addEntity(Entity entity){
        this.entitiesToAdd.add(entity);
    }
    public void addParticle(Particle particle){
        this.particles.add(particle);
    }
    public void playSoundEffect(JSSoundEffectRegistry.SoundEffect soundEffect, int x, int y){
        getServer().getWSServer().broadcastInWorld(new SoundEffectMessage(soundEffect, x, y), this);
    }
    public void spawnExplosion(float x, float y, float power, float radius){
        addParticle(new Particle("explosion", 60, x, y).addNumber("power", power).addNumber("radius", radius));
        for(Entity entity : entities){
            if(entity.getPhysicalObject() == null)
                continue;
            float dist = (float) Math.sqrt(entity.getLocation().distanceToNS(new Location(x, y, this)));
            if(dist < radius){
                float damage = power*(1-(dist/radius));
                Vector2 vector = new Vector2(entity.getLocation().x()-x, entity.getLocation().y()-y);
                vector.normalise();
                vector.multiply(damage*3);
                entity.applyKnockback((float) vector.x, (float) vector.y);
                entity.damage(-damage, EDamageType.EXPLOSION);
            }
        }
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
    public boolean remove(){
        if(lobby)
            return false;
        this.removed = true;
        Location spawn = this.server.getLobby().getSpawn();
        for(Entity entity : this.entities){
            if(entity instanceof PlayerEntity)
                entity.teleport(spawn);
        }
        this.entities.clear();
        return true;
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
