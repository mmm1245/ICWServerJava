package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonObject;

public class ControllerDataMessage extends Message {
    public static final String TYPE = "controllerData";

    public final Entity controllingEntity;
    public final float speed;
    public final float gravity;
    public final float jumpPower;
    public final boolean noClip;
    public final boolean isTopDown;
    public final boolean frozen;
    public ControllerDataMessage(Entity controllingEntity, float speed, float gravity, float jumpPower, boolean noClip, boolean isTopDown, boolean frozen) {
        this.controllingEntity = controllingEntity;
        this.speed = speed;
        this.gravity = gravity;
        this.jumpPower = jumpPower;
        this.noClip = noClip;
        this.isTopDown = isTopDown;
        this.frozen = frozen;
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("controllingId", controllingEntity.id);
        json.addProperty("speed", speed);
        json.addProperty("gravity", gravity);
        json.addProperty("jumpPower", jumpPower);
        json.addProperty("noClip", noClip);
        json.addProperty("isTopDown", isTopDown);
        json.addProperty("frozen", frozen);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
