package com.github.industrialcraft.icwserver.net.messages;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.script.JSSoundEffectRegistry;
import com.google.gson.JsonObject;

public class SoundEffectMessage extends Message {
    public static final String TYPE = "soundEffect";

    public JSSoundEffectRegistry.SoundEffect sound;
    public int x;
    public int y;
    public SoundEffectMessage(JSSoundEffectRegistry.SoundEffect sound, int x, int y) {
        this.sound = sound;
        this.x = x;
        this.y = y;
    }
    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("sound", this.sound.name);
        json.addProperty("x", this.x);
        json.addProperty("y", this.y);
        return json;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
