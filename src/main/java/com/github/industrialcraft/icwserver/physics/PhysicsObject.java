package com.github.industrialcraft.icwserver.physics;

import com.github.industrialcraft.icwserver.util.EWorldOrientation;
import com.github.industrialcraft.icwserver.world.entity.Entity;

import java.util.function.Predicate;

public class PhysicsObject {
    protected final Entity entity;
    protected float hitboxW;
    protected float hitboxH;
    public final EPhysicsLayer layer;
    public float knockbackX;
    public float knockbackY;
    public float knockbackFalloff;
    public float knockbackEffectivity;
    public float gravity;
    public PhysicsObject(Entity entity, float hitboxW, float hitboxH, EPhysicsLayer layer) {
        this.entity = entity;
        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;
        this.layer = layer;
        this.knockbackX = 0;
        this.knockbackY = 0;
        this.knockbackFalloff = 0.1f;
        this.knockbackEffectivity = 1;
        this.gravity = -2;
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
    public void applyKnockback(float x, float y){
        this.knockbackX += x*knockbackEffectivity;
        this.knockbackY += y*knockbackEffectivity;
    }
    public void resetKnockback(){
        this.knockbackX = 0;
        this.knockbackY = 0;
    }
    public void tick(){
        moveBy(this.knockbackX, this.knockbackY + (entity.getLocation().world().orientation==EWorldOrientation.SIDE?gravity:0));
        this.knockbackX -= this.knockbackX*this.knockbackFalloff;
        this.knockbackY -= this.knockbackY*this.knockbackFalloff;
    }
    public boolean canMoveTo (float x, float y){
        if(layer == EPhysicsLayer.PROJECTILE || layer == EPhysicsLayer.TRANSPARENT)
            return true;
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
    public Entity collidesAt(float x, float y, Predicate<Entity> entityPredicate, boolean checkLayer){
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
            if((checkLayer?collides(this.layer, physics2.layer):true)&&Collisions.AABB(x, y, hitboxW, hitboxH, entity.getLocation().x(), entity.getLocation().y(), physics2.hitboxW, physics2.hitboxH))
                return entity;
        }
        return null;
    }
    public Entity collides(Predicate<Entity> entityPredicate, boolean checkLayer){
        for(Entity entity : entity.getLocation().world().getEntities()){
            if(this.entity.id == entity.id)
                continue;
            PhysicsObject physics2 = entity.getPhysicalObject();
            if(physics2==null)
                continue;
            if(!entityPredicate.test(entity))
                continue;
            if((checkLayer?collides(this.layer, physics2.layer):true)&&Collisions.AABB(this.entity.getLocation().x(), this.entity.getLocation().y(), hitboxW, hitboxH, entity.getLocation().x(), entity.getLocation().y(), physics2.hitboxW, physics2.hitboxH))
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
        if(layer1==EPhysicsLayer.PROJECTILE&&layer2==EPhysicsLayer.PROJECTILE)
            return false;
        return layer1==EPhysicsLayer.WALL||layer2==EPhysicsLayer.WALL;
    }
    public PhysicsObject clone(Entity entity){
        PhysicsObject po = new PhysicsObject(entity, hitboxW, hitboxH, layer);
        po.knockbackX = this.knockbackX;
        po.knockbackY = this.knockbackY;
        po.knockbackFalloff = this.knockbackFalloff;
        po.knockbackEffectivity = this.knockbackEffectivity;
        return po;
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
