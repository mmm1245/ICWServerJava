package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.net.messages.ControllerDataMessage;

public class PlayerAbilities {
    public int speed;
    public int gravity;
    public int jumpPower;
    public boolean noClip;
    public boolean topDown;
    public boolean frozen;
    public PlayerAbilities(int speed, int gravity, int jumpPower, boolean noClip, boolean topDown, boolean frozen) {
        this.speed = speed;
        this.gravity = gravity;
        this.jumpPower = jumpPower;
        this.noClip = noClip;
        this.topDown = topDown;
        this.frozen = frozen;
    }
    public void toggleNoClip(){
        this.noClip = !this.noClip;
    }
    public void toggleTopDown(){
        this.topDown = !this.topDown;
    }
    public void toggleFrozen(){
        this.frozen = !this.frozen;
    }
    public ControllerDataMessage createControllerMessage(Entity controlling){
        return new ControllerDataMessage(controlling, speed, gravity, jumpPower, noClip, topDown, frozen);
    }
}
