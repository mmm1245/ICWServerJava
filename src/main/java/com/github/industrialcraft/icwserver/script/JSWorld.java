package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.Particle;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

import java.util.Collections;
import java.util.List;

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
    public List<Entity> entities(){
        return world.getEntities();
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

    public World getInternal(){
        return this.world;
    }
}
