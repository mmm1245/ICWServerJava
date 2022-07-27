package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.util.playerState.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JSPlayerStateRegistry {
    private HashMap<String, PlayerState> playerStates;
    public JSPlayerStateRegistry() {
        this.playerStates = new HashMap<>();
    }
    public PlayerStateTemplate createTemplate(String id){
        if(playerStates.containsKey(id))
            throw new IllegalStateException(id + " already registered");
        return new PlayerStateTemplate(id);
    }
    public Map<String, PlayerState> getPlayerStates(){
        return Collections.unmodifiableMap(playerStates);
    }

    public class PlayerStateTemplate {
        private String id;
        private ArrayList<TauntPart> parts;
        private boolean motionBlocking;
        public PlayerStateTemplate(String id) {
            this.id = id;
            this.parts = new ArrayList<>();
        }
        public PlayerStateTemplate addPart(String asset, int length){
            this.parts.add(new TauntPart(asset, length));
            return this;
        }
        public PlayerStateTemplate markMotionBlocking(){
            this.motionBlocking = true;
            return this;
        }
        public PlayerState register(){
            if(playerStates.containsKey(id))
                throw new IllegalStateException(id + " already registered");
            if(parts.size() <= 0)
                throw new IllegalStateException("player state must have at least 1 part");
            PlayerState taunt = new PlayerState(id, Collections.unmodifiableList(parts), this.motionBlocking);
            playerStates.put(id, taunt);
            return taunt;
        }

        public record TauntPart(String asset, int length) {}
    }
}
