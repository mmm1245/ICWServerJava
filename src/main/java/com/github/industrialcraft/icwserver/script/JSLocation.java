package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.World;

public class JSLocation {
    private Location location;
    public JSLocation(Location location) {
        this.location = location;
    }
    public JSLocation(float x, float y, JSWorld world){
        this.location = new Location(x, y, world.getInternal());
    }
    public JSLocation(float x, float y, World world){
        this.location = new Location(x, y, world);
    }
    public JSLocation withX(float x) {
        return new JSLocation(x, this.location.y(), this.location.world());
    }
    public JSLocation withY(float y) {
        return new JSLocation(this.location.x(), y, this.location.world());
    }
    public JSLocation withXY(float x, float y) {
        return new JSLocation(x, y, this.location.world());
    }
    public JSLocation addXY(float addX, float addY){
        return new JSLocation(location.x()+addX, location.y()+addY, location.world());
    }
    public JSLocation withWorld(World world) {
        return new JSLocation(location.x(), location.y(), world);
    }

    public float distanceToNS(JSLocation other){
        float a = Math.abs(location.x()-other.location.x());
        float b = Math.abs(location.y()-other.location.y());
        return a*a + b*b;
    }

    public float x(){
        return location.x();
    }
    public float y(){
        return location.y();
    }
    public JSWorld world(){
        return new JSWorld(location.world());
    }

    public Location getInternal(){
        return this.location;
    }
}
