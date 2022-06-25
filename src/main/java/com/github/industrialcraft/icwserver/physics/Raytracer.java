package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import mikera.vectorz.Vector2;

import java.util.function.Predicate;

public class Raytracer {
    public static Entity raytrace(Location startLocation, float angle, int length, Predicate<Entity> collidePredicate){
        Vector2 start = new Vector2(startLocation.x(), startLocation.y());
        Vector2 end = new Vector2((int) (Math.cos(Math.toRadians((angle + 360) % 360)) * 10), (int) (Math.sin(Math.toRadians((angle + 360) % 360)) * 10));
        end.normalise();
        end.multiply(length);
        end.add(start);
        return Collisions.lineIntersection(start, end, startLocation.world(), collidePredicate);
    }
}
