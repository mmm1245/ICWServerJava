package com.github.industrialcraft.icwserver.script.event;

import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

public class Events {
    public final Event PLAYER_JOIN = new Event();
    public final Event PLAYER_LEAVE = new Event();
    public final Event CREATE_WORLD = new Event();
    public final Event WORLD_TICK = new Event();
    public final Event SERVER_TICK = new Event();
    public final Event START_SERVER = new Event();
    public final Event CUSTOM_MESSAGE_RECEIVED = new Event();
}
