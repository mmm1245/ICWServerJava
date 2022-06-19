package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.inventory.Items;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.WSServer;
import com.github.industrialcraft.icwserver.net.messages.*;
import com.github.industrialcraft.icwserver.physics.Collisions;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.*;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.data.IDamagable;
import com.github.industrialcraft.icwserver.world.entity.data.IOnPlayerInteract;
import com.github.industrialcraft.inventorysystem.ItemStack;
import mikera.vectorz.Vector2;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;

public class GameServer extends Thread{
    protected final WSServer server;
    protected final ArrayList<World> worlds;
    protected int entityIdGenerator;
    protected int worldIdGenerator;
    public GameServer(InetSocketAddress address) {
        this.server = new WSServer(address, this);
        this.server.setReuseAddr(true);
        this.worlds = new ArrayList<>();
        this.worlds.add(new World(true, this));
        this.entityIdGenerator = 0;
        this.worldIdGenerator = 0;

        new PlatformEntity(new Location(0, -30, worlds.get(0)), 100, 5);
        new GeneratorEntity(new Location(20, 0, worlds.get(0)), 50, new ItemStack(Items.STONE, 10));
    }
    public World createWorld(){
        World world = new World(false, this);
        this.worlds.add(world);
        return world;
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
                this.server.broadcastInWorld(new MapDataMessage(world), world);
            }
            for(ClientConnection connection : this.server.getClientConnections()){
                if(connection.player != null)
                    connection.send(new PlayerInventoryMessage(connection.player.getInventory()));
            }
            while(server.hasMessage()){
                Pair<ClientConnection,Message> polled = server.pollMessage();
                ClientConnection connection = polled.first;
                Message pMsg = polled.second;
                if(pMsg instanceof LoginMessage msg){
                    if(connection.player!=null) {
                        connection.disconnect("already logged in");
                        continue;
                    }
                    PlayerEntity pl = new PlayerEntity(getLobby().getSpawn(), connection);
                    connection.profile = new PlayerProfile(msg.username, UUID.randomUUID());
                    connection.player = pl;
                    connection.send(new ControllingEntityMessage(pl));
                    continue;
                }
                if(connection.player==null){
                    connection.disconnect("first message must be login");
                    continue;
                }
                if(pMsg instanceof ClientPlayerPositionMessage msg){
                    connection.player.setLocationFromClient(msg);
                }
                if(pMsg instanceof PlayerAttackMessage msg){
                    System.out.println("attack: " + msg);
                    Vector2 start = new Vector2(connection.player.getLocation().x(), connection.player.getLocation().y());
                    Vector2 end = new Vector2((int)(Math.cos(Math.toRadians((msg.angle+360)%360))*10), (int)(Math.sin(Math.toRadians((msg.angle+360)%360))*10));
                    end.normalise();
                    end.multiply(50);
                    end.add(start);
                    Entity entity = Collisions.lineIntersection(start, end, connection.player.getLocation().world(), ent -> ent.id != connection.player.id);
                    if(entity instanceof IDamagable damagable){
                        damagable.damage(-15, EDamageType.FIST);
                    }
                }
                if(pMsg instanceof InteractEntityMessage msg){
                    Entity entity = connection.player.getLocation().world().byId(msg.entityId);
                    if(entity != null && entity instanceof IOnPlayerInteract interactEvent){
                        interactEvent.onPlayerInteract(connection.player, msg);
                    }
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
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
