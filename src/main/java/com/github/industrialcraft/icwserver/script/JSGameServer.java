package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JSGameServer {
    GameServer gameServer;
    public JSGameServer(GameServer server){
        this.gameServer = server;
    }
    public JSWorld createWorld(){
        return new JSWorld(this.gameServer.createWorld());
    }
    public JSWorld worldById(int id){
        return new JSWorld(this.gameServer.worldById(id));
    }
    public JSWorld lobby(){
        return new JSWorld(this.gameServer.getLobby());
    }
    public List<JSWorld> worlds(){
        return this.gameServer.getWorlds().stream().map(world -> new JSWorld(world)).collect(Collectors.toUnmodifiableList());
    }

    public void broadcastMessage(Message message){
        this.gameServer.getWSServer().broadcast(message);
    }
    public void broadcastMessageInWorld(Message message, JSWorld world){
        this.gameServer.getWSServer().broadcastInWorld(message, world.getInternal());
    }

    public List<ClientConnection> clientConnections() {
        return this.gameServer.getWSServer().getClientConnections();
    }
    public List<PlayerEntity> players() {
        return this.gameServer.getWSServer().getClientConnections().stream()
                .map(clientConnection -> clientConnection.player)
                .filter(player -> player != null)
                .collect(Collectors.toUnmodifiableList());
    }
}