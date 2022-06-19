package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.IEntityLambda;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.inventorysystem.ItemStack;

public class GeneratorEntity extends DamageableEntity implements IPhysicalEntity{
    private int healthPoints;
    private ItemStack item;
    private PhysicsObject physicsObject;
    public GeneratorEntity(Location location, int healthPoints, ItemStack item) {
        super(location);
        this.healthPoints = healthPoints;
        this.health = getMaxHealth();
        this.item = item;
        this.physicsObject = new PhysicsObject(this, 20, 20, EPhysicsLayer.OBJECT);
    }

    @Override
    public void tick() {

    }

    @Override
    public String getType() {
        return "generator";
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return physicsObject;
    }

    @Override
    public float getMaxHealth() {
        return healthPoints;
    }

    @Override
    public void damage(float damage, EDamageType type) {
        float healthNew = getHealth()+(damage*getDamageTypeModifier(type));
        if(healthNew > getMaxHealth())
            healthNew = getMaxHealth();
        setHealth(healthNew);
    }

    @Override
    public void setHealth(float health) {
        if(health <= 0){
            new ItemStackEntity(location, item.clone());
            this.health = getMaxHealth();
        } else {
            this.health = health;
        }
    }
}
