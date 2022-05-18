package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.types.EntityData;
import com.github.industrialcraft.icwserver.world.entity.types.EntityType;

public class Entity {
    private Location location;
    private final EntityType type;
    private final EntityData data;
    private boolean removed;
    private final int id;
    public Entity(EntityType type, Location location) {
        this.id = location.world().getServer().generateIDEntity();
        this.removed = false;
        this.type = type;
        this.data = type.createData(this);
        this.location = location;
        location.world().addEntity(this);
    }
    public void tick(){
        type.tick(this);
    }
    public void teleport(float x, float y){
        this.location = this.location.withXY(x, y);
    }
    public void teleport(Location location){
        if(location.world()!=this.location.world())
            location.world().addEntity(this);
        this.location = location;
    }
    public void remove(){
        this.removed = true;
    }
    public boolean isRemoved() {
        return removed;
    }
    public EntityType getType() {
        return type;
    }
    public EntityData getData() {
        return data;
    }
    public Location getLocation() {
        return location;
    }
    public int getId() {
        return id;
    }
}
