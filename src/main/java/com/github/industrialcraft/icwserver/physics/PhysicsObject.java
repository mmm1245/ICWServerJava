package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.world.entity.Entity;

import java.util.function.Predicate;

public class PhysicsObject {
    protected final Entity entity;
    protected float hitboxW;
    protected float hitboxH;
    protected final EPhysicsLayer layer;
    public PhysicsObject(Entity entity, int hitboxW, int hitboxH, EPhysicsLayer layer) {
        this.entity = entity;
        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;
        this.layer = layer;
    }
    public boolean moveBy(float x, float y){
        float newX = entity.getLocation().x()+x;
        float newY = entity.getLocation().y()+y;
        if(canMoveTo(newX, newY)){
            entity.teleport(newX, newY);
            return true;
        }
        return false;
    }
    public boolean canMoveTo(float x, float y){
        for(Entity entity : entity.getLocation().world().getEntities()){
            if(this.entity==entity)
                continue;
            PhysicsObject physics2 = entity.getPhysicalObject();
            if(physics2==null)
                continue;
            if(Collisions.AABB(this.entity.getLocation().x(), this.entity.getLocation().y(), hitboxW, hitboxH, entity.getLocation().x(), entity.getLocation().y(), physics2.hitboxW, physics2.hitboxH))
                continue;
            if(collides(this.layer, physics2.layer) && Collisions.AABB(x, y, hitboxW, hitboxH, entity.getLocation().x(), entity.getLocation().y(), physics2.hitboxW, physics2.hitboxH))
                return false;
        }
        return true;
    }
    public Entity collidesAt(float x, float y, Predicate<Entity> entityPredicate){
        for(Entity entity : entity.getLocation().world().getEntities()){
            if(this.entity==entity)
                continue;
            if(this.entity.id == entity.id)
                continue;
            if(!entityPredicate.test(entity))
                continue;
            PhysicsObject physics2 = entity.getPhysicalObject();
            if(physics2==null)
                continue;
            if(Collisions.AABB(x, y, hitboxW, hitboxH, entity.getLocation().x(), entity.getLocation().y(), physics2.hitboxW, physics2.hitboxH))
                return entity;
        }
        return null;
    }
    public void resize(float hitboxW, float hitboxH){
        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;
    }

    protected static boolean collides(EPhysicsLayer layer1, EPhysicsLayer layer2){
        if(layer1==EPhysicsLayer.TRANSPARENT||layer2==EPhysicsLayer.TRANSPARENT)
            return false;
        return layer1==EPhysicsLayer.WALL||layer2==EPhysicsLayer.WALL;
    }
    public Entity getEntity() {
        return entity;
    }
    public float getHitboxW() {
        return hitboxW;
    }
    public float getHitboxH() {
        return hitboxH;
    }
    public EPhysicsLayer getLayer() {
        return layer;
    }
}
