package com.github.industrialcraft.icwserver.world.entity.data;

public enum EDamageType {
    HEAL_POTION(false, false, false, true, false, false),
    FIX(false, false, false, true, false, false),
    NATURAL(false, false, false, true, false, false),
    FIST(false, true, true, false, false, false),
    BULLET(true, false, true, false, false, true),
    THROWN(false, false, true, false, false, false),
    FIRE(false, false, false, false, true, false),
    LAVA(false, false, false, false, true, false),
    WATER(false, false, false, false, false, false),
    EXPLOSION(true, false, false, false, true, false),
    BLADE(false, false, false, false, false, true),
    SHARP(false, false, true, false, false, true),
    HEAVY_BLADE(true, false, false, false, false, true),
    HEAVY_SHARP(true, false, true, false, false, true);
    public final boolean isHeavy;
    public final boolean isLight;
    public final boolean isSmall;
    public final boolean isHeal;
    public final boolean isHot;
    public final boolean isDamage;
    public final boolean isPiercing;
    EDamageType(boolean isHeavy, boolean isLight, boolean isSmall, boolean isHeal, boolean isHot, boolean isPiercing) {
        this.isHeavy = isHeavy;
        this.isLight = isLight;
        this.isSmall = isSmall;
        this.isHeal = isHeal;
        this.isDamage = !isHeal;
        this.isHot = isHot;
        this.isPiercing = isPiercing;
    }
}
