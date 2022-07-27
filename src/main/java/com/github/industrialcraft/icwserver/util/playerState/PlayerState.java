package com.github.industrialcraft.icwserver.util.playerState;

import com.github.industrialcraft.icwserver.script.JSPlayerStateRegistry;

import java.util.List;

public class PlayerState {
    public final String id;
    public final List<JSPlayerStateRegistry.PlayerStateTemplate.TauntPart> parts;
    public final boolean motionBlocking;
    public PlayerState(String id, List<JSPlayerStateRegistry.PlayerStateTemplate.TauntPart> parts, boolean motionBlocking) {
        this.id = id;
        this.parts = parts;
        this.motionBlocking = motionBlocking;
    }
}
