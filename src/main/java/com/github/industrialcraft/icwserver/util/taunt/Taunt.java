package com.github.industrialcraft.icwserver.util.taunt;

import com.github.industrialcraft.icwserver.script.JSTauntRegistry;

import java.util.List;

public class Taunt {
    public final String id;
    public final List<JSTauntRegistry.TauntTemplate.TauntPart> parts;
    public Taunt(String id, List<JSTauntRegistry.TauntTemplate.TauntPart> parts) {
        this.id = id;
        this.parts = parts;
    }
}
