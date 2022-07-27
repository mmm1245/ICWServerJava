package com.github.industrialcraft.icwserver.script;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSSoundEffectRegistry {
    private HashMap<String, SoundEffect> soundEffects;
    public JSSoundEffectRegistry() {
        this.soundEffects = new HashMap<>();
    }
    public SoundEffect register(String name, String asset){
        return soundEffects.put(name, new SoundEffect(name, asset));
    }
    public Map<String,SoundEffect> getSoundEffects(){
        return Collections.unmodifiableMap(soundEffects);
    }
    public class SoundEffect{
        public final String name;
        public final String asset;
        public SoundEffect(String name, String asset) {
            this.name = name;
            this.asset = asset;
        }
    }
}
