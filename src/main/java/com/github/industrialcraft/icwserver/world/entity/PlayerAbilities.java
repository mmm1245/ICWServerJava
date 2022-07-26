package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.net.messages.ControllerDataMessage;

public class PlayerAbilities {
    public int speed;
    public int gravity;
    public int jumpPower;
    public boolean noClip;
    public boolean topDown;
    public PlayerAbilities() {
        reset();
    }
    public void reset(){
        this.speed = 2;
        this.gravity = 3;
        this.jumpPower = 5;
        this.noClip = false;
        this.topDown = false;
    }
    public ControllerDataMessage createControllerMessage(Entity controlling, boolean frozen){
        return new ControllerDataMessage(controlling, speed, gravity, jumpPower, noClip, topDown, frozen);
    }
}
