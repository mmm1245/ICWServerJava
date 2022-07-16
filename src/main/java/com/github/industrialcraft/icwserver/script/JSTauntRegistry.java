package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.util.taunt.Taunt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSTauntRegistry {
    private HashMap<String, Taunt> taunts;
    public JSTauntRegistry() {
        this.taunts = new HashMap<>();
    }
    public TauntTemplate createTemplate(String id){
        if(taunts.containsKey(id))
            throw new IllegalStateException(id + " already registered");
        return new TauntTemplate(id);
    }
    public Map<String,Taunt> getTaunts(){
        return Collections.unmodifiableMap(taunts);
    }

    public class TauntTemplate{
        private String id;
        private ArrayList<TauntPart> parts;
        public TauntTemplate(String id) {
            this.id = id;
            this.parts = new ArrayList<>();
        }
        public TauntTemplate addPart(String asset, int length){
            this.parts.add(new TauntPart(asset, length));
            return this;
        }
        public Taunt register(){
            if(taunts.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            if(parts.size() <= 0)
                throw new IllegalStateException("taunt must have at least 1 part");
            Taunt taunt = new Taunt(id, Collections.unmodifiableList(parts));
            taunts.put(id, taunt);
            return taunt;
        }

        public record TauntPart(String asset, int length) {}
    }
}
