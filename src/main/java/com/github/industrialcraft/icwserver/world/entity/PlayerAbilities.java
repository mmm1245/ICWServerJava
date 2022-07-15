package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.net.messages.ControllerDataMessage;

public class PlayerAbilities {
    public int speed;
    public int gravity;
    public int jumpPower;
    public boolean noClip;
    public boolean topDown;
    public PlayerAbilities(int speed, int gravity, int jumpPower, boolean noClip, boolean topDown) {
        this.speed = speed;
        this.gravity = gravity;
        this.jumpPower = jumpPower;
        this.noClip = noClip;
        this.topDown = topDown;
    }
    public void toggleNoClip(){
        this.noClip = !this.noClip;
    }
    public void toggleTopDown(){
        this.topDown = !this.topDown;
    }
    public ControllerDataMessage createControllerMessage(Entity controlling, boolean frozen){
        return new ControllerDataMessage(controlling, speed, gravity, jumpPower, noClip, topDown, frozen);
    }
}
