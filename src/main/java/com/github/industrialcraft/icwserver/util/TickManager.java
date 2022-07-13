package com.github.industrialcraft.icwserver.util;

public class TickManager {
    private boolean frozen;
    private int warp;
    public TickManager(){
        this.frozen = false;
        this.warp = 0;
    }
    public void toggleFreeze(){
        this.frozen = !frozen;
    }
    public boolean shouldTick(){
        return (!frozen) || warp > 0;
    }
    public boolean isFrozen() {
        return frozen;
    }
    public int getWarpTime() {
        return warp;
    }
    public void decreaseWarpTime(){
        warp--;
    }
    public void setWarpTime(int warp) {
        this.warp = warp;
    }
}
