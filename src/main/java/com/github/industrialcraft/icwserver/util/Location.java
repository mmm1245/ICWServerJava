package com.github.industrialcraft.icwserver.util;

import com.github.industrialcraft.icwserver.world.World;

public record Location(float x, float y, World world) {
    public Location withX(float x) {
        return new Location(x, this.y, this.world);
    }
    public Location withY(float y) {
        return new Location(this.x, y, this.world);
    }
    public Location withXY(float x, float y) {
        return new Location(x, y, this.world);
    }
    public Location addXY(float addX, float addY){
        return new Location(x+addX, y+addY, world);
    }
    public Location withWorld(World world) {
        return new Location(this.x, this.y, world);
    }

    public float distanceToNS(Location other){
        float a = Math.abs(this.x-other.x);
        float b = Math.abs(this.y-other.y);
        return a*a + b*b;
    }
}
