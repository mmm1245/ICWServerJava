package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.Particle;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.ItemStackEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.js.EntityFromJS;
import org.w3c.dom.Entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JSWorld {
    private World world;
    public JSWorld(World world) {
        this.world = world;
    }

    public JSLocation spawnpoint(){
        return new JSLocation(world.getSpawn());
    }
    public void remove(){
        world.remove();
    }
    public List<EntityFromJS> entities(){
        return world.getEntities().stream()
                .filter(entity -> (entity instanceof EntityFromJS))
                .map(entity -> ((EntityFromJS)entity))
                .collect(Collectors.toUnmodifiableList());
    }
    public List<JSPlayer> players(){
        return world.getEntities().stream()
                .filter(entity -> (entity instanceof PlayerEntity))
                .map(entity -> new JSPlayer((PlayerEntity)entity))
                .collect(Collectors.toUnmodifiableList());
    }
    public List<ItemStackEntity> items(){
        return world.getEntities().stream()
                .filter(entity -> (entity instanceof ItemStackEntity))
                .map(entity -> ((ItemStackEntity)entity))
                .collect(Collectors.toUnmodifiableList());
    }
    public List<Particle> particles(){
        return world.getParticles();
    }
    public boolean isRemoved(){
        return world.isRemoved();
    }
    public JSLocation location(int x, int y){
        return new JSLocation(x, y, world);
    }
    public Particle spawnParticle(String type, int x, int y, int lifetime){
        Particle particle = new Particle(type, lifetime, x, y);
        world.addParticle(particle);
        return particle;
    }

    public void setData(Object data){
        world.data = data;
    }
    public Object getData(){
        return world.data;
    }

    public boolean lobby(){
        return world.lobby;
    }

    public World getInternal(){
        return this.world;
    }
}
