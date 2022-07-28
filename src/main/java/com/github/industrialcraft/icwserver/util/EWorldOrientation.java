package com.github.industrialcraft.icwserver.util;

public enum EWorldOrientation {
    SIDE,
    TOP_DOWN;
    public static EWorldOrientation byName(String name){
        for(EWorldOrientation worldOrientation : values()){
            if(worldOrientation.name().equalsIgnoreCase(name))
                return worldOrientation;
        }
        return null;
    }
}
