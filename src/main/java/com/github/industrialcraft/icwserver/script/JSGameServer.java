package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.physics.Raytracer;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JSGameServer {
    private GameServer gameServer;
    public JSGameServer(GameServer server){
        this.gameServer = server;
    }
    public long ticksLasted() {
        return gameServer.ticksLasted();
    }
    public JSPlayer playerByName(String name){
        PlayerEntity player = gameServer.playerByName(name);
        if(player == null)
            return null;
        else
            return new JSPlayer(player);
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
    public Entity raytrace(JSLocation start, float angle, int length, ScriptObjectMirror predicate){
        return Raytracer.raytrace(start.getInternal(), angle, length, entity -> predicate.call(entity).toString().equalsIgnoreCase("true"));
    }
    public void broadcastMessage(Message message){
        this.gameServer.getWSServer().broadcast(message);
    }
    public void broadcastMessageInWorld(Message message, JSWorld world){
        this.gameServer.getWSServer().broadcastInWorld(message, world.getInternal());
    }
    public void spawnExplosion(JSLocation location, int power, int radius){
        location.world().getInternal().spawnExplosion(location.x(), location.y(), power, radius);
    }
    public void spawnExplosion(Location location, int power, int radius){
        location.world().spawnExplosion(location.x(), location.y(), power, radius);
    }

    public List<JSPlayer> players() {
        return this.gameServer.getWSServer().getClientConnections().stream()
                .map(clientConnection -> clientConnection.player)
                .filter(player -> player != null)
                .map(player -> new JSPlayer(player))
                .collect(Collectors.toUnmodifiableList());
    }
    public JSPlayer playerById(int id){
        PlayerEntity player = gameServer.playerById(id);
        if(player == null)
            return null;
        else
            return new JSPlayer(player);
    }

    public GameServer getInternal(){
        return this.gameServer;
    }
}
