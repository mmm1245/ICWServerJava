package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.WSServer;
import com.github.industrialcraft.icwserver.net.messages.ClientPlayerPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.EntityPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.LoginMessage;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.Entities;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.types.PlayerEntityData;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class GameServer extends Thread{
    private final WSServer server;
    private final ArrayList<World> worlds;
    private int entityIdGenerator;
    private int worldIdGenerator;
    public GameServer(InetSocketAddress address) {
        this.server = new WSServer(address, this);
        this.worlds = new ArrayList<>();
        this.worlds.add(new World(true, this));
        this.entityIdGenerator = 0;
        this.worldIdGenerator = 0;
    }
    public World createWorld(){
        World world = new World(false, this);
        this.worlds.add(world);
        return world;
    }
    public Entity createPlayer(){
        return new Entity(Entities.PLAYER, getLobby().getSpawn());
    }

    @Override
    public void run() {
        server.start();
        while(true) {
            this.worlds.removeIf(world -> world.isRemoved());
            for (World world : this.worlds) {
                world.tick();
            }
            for (World world : this.worlds){
                for(Entity entity : world.getEntities()){
                    this.server.broadcast(new EntityPositionMessage(entity));
                }
            }
            while(server.hasMessage()){
                Pair<ClientConnection,Message> polled = server.pollMessage();
                ClientConnection connection = polled.first;
                Message pMsg = polled.second;
                if(pMsg instanceof LoginMessage msg){
                    if(connection.getPlayer()!=null) {
                        connection.disconnect("already logged in");
                        continue;
                    }
                    Entity pl = createPlayer();
                    PlayerEntityData playerData = pl==null?null:(PlayerEntityData)pl.getData();
                    playerData.name = msg.username;
                    playerData.clientConnection = connection;
                    connection.setPlayer(pl);
                    continue;
                }
                if(connection.getPlayer()==null){
                    connection.disconnect("first message must be login");
                    continue;
                }
                if(pMsg instanceof ClientPlayerPositionMessage msg){
                    //todo: anticheat
                    connection.getPlayer().teleport(msg.x, msg.y);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public World worldById(int id){
        for(World world : this.worlds){
            if(world.getId()==id)
                return world;
        }
        return null;
    }
    public int generateIDEntity(){
        return ++this.entityIdGenerator;
    }
    public int generateIDWorld(){
        return ++this.worldIdGenerator;
    }

    public World getLobby() {
        return this.worlds.get(0);
    }
}
