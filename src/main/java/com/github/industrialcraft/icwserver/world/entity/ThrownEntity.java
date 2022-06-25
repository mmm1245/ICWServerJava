package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.inventory.ItemSerializer;
import com.github.industrialcraft.icwserver.physics.Collisions;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.data.IDamagable;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class ThrownEntity extends Entity implements IPhysicalEntity{
    private PhysicsObject physicsObject;
    private float velX;
    private float velY;
    private int shooterId;
    private ItemStack projectileItem;
    public ThrownEntity(Location location, float velX, float velY, int shooterId, ItemStack projectileItem) {
        super(location);
        this.shooterId = shooterId;
        this.projectileItem = projectileItem;
        this.physicsObject = new PhysicsObject(this, 6, 6, EPhysicsLayer.PROJECTILE);
        this.velX = velX;
        this.velY = velY;
    }

    @Override
    public void tick() {
        //this.velX = velX*.95f;
        //this.velY = velY*.95f;
        this.velY -= 0.15;
        if(this.velY < -5)
            this.velY = -5;
        this.velX -= (velX>0?1:-1)*0.02;
        Entity collision = this.physicsObject.collidesAt(location.x() + velX, location.y() + velY, entity -> (Collisions.bulletCanHit(entity))&&entity.id!=shooterId);
        if(collision != null){
            if(collision instanceof IDamagable damagable)
                damagable.damage(-10, EDamageType.THROWN);
            kill();
        }
        this.physicsObject.moveBy(velX, velY);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.add("item", ItemSerializer.toJson(projectileItem));
        return json;
    }

    @Override
    public void onDeath() {
        new ItemStackEntity(this.getLocation(), projectileItem.clone());
    }

    @Override
    public String getType() {
        return "thrownEntity";
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return physicsObject;
    }
}
