package com.github.industrialcraft.icwserver.world.entity.data;

public interface IDamagable extends IKillable{
    void setHealth(float health);
    float getHealth();

    float getMaxHealth();

    default float getDamageTypeModifier(EDamageType type){
        return 1;
    }

    default void damage(float damage, EDamageType type){
        float healthNew = getHealth()+(damage*getDamageTypeModifier(type));
        if(healthNew <= 0) {
            healthNew = 0;
            kill();
        }
        if(healthNew > getMaxHealth())
            healthNew = getMaxHealth();
        setHealth(healthNew);
    }
}
