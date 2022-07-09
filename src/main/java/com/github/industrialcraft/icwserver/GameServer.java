package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.inventory.Items;
import com.github.industrialcraft.icwserver.inventory.data.IActionProcessingInventory;
import com.github.industrialcraft.icwserver.inventory.data.IPlayerAttackHandler;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.WSServer;
import com.github.industrialcraft.icwserver.net.messages.*;
import com.github.industrialcraft.icwserver.physics.Raytracer;
import com.github.industrialcraft.icwserver.script.ScriptingManager;
import com.github.industrialcraft.icwserver.script.event.Events;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.*;
import com.github.industrialcraft.icwserver.world.entity.craftingStation.WoodWorkingStation;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.icwserver.world.entity.data.IDamagable;
import com.github.industrialcraft.icwserver.world.entity.data.IPlayerInteractHandler;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameServer extends Thread{
    protected final WSServer server;
    protected final ArrayList<World> worlds;
    protected int entityIdGenerator;
    protected int worldIdGenerator;
    protected ScriptingManager scriptingManager;
    public GameServer(InetSocketAddress address) {
        this.server = new WSServer(address, this);
        this.server.setReuseAddr(true);
        this.worlds = new ArrayList<>();
        this.worlds.add(new World(true, this));
        this.entityIdGenerator = 0;
        this.worldIdGenerator = 0;

        new PlatformEntity(new Location(0, -30, worlds.get(0)), 100, 5);
        new WoodWorkingStation(new Location(50, 0, worlds.get(0)));
    }
    public World createWorld(){
        World world = new World(false, this);
        this.worlds.add(world);
        return world;
    }

    public WSServer getWSServer() {
        return server;
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
                    connection.send(new PlayerInventoryMessage(connection.player.getInventory(), connection.player.getOpenedInventory(), connection.player.getHandItemStack(), connection.player.getHealth()));
            }
            while(server.hasMessage()){
                Pair<ClientConnection,Message> polled = server.pollMessage();
                ClientConnection connection = polled.first;
                Message pMsg = polled.second;
                if(!connection.getConn().isOpen())
                    continue;
                if(pMsg instanceof LoginMessage msg){
                    if(connection.player!=null) {
                        connection.disconnect("already logged in");
                        continue;
                    }
                    PlayerEntity pl = new PlayerEntity(getLobby().getSpawn(), connection);
                    connection.profile = new RPlayerProfile(msg.username, UUID.randomUUID());
                    connection.player = pl;
                    connection.send(new ControllingEntityMessage(pl));
                    getEvents().PLAYER_JOIN.call(new Object[]{pl});
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
                    ItemStack hand = connection.player.getHandItemStack();
                    if(hand == null || !((Item) hand.getItem()).onAttackHandlerCall(connection.player, hand, msg)) {
                        Entity entity = Raytracer.raytrace(connection.player.getLocation().addXY(2, 15), msg.angle, 50, ent -> ent.id != connection.player.id);
                        if (entity instanceof IDamagable damagable) {
                            damagable.damage(-15, EDamageType.FIST);
                        }
                    }
                }
                if(pMsg instanceof InteractEntityMessage msg){
                    Entity entity = connection.player.getLocation().world().byId(msg.entityId);
                    if(entity != null && entity instanceof IPlayerInteractHandler interactEvent){
                        interactEvent.onPlayerInteract(connection.player, msg);
                    }
                }
                if(pMsg instanceof PlayerClickSlotMessage msg){
                    Inventory inv = msg.secondInventory?connection.player.getOpenedInventory():connection.player.getInventory();
                    if(inv==null)
                        continue;
                    if(msg.slot >= 0 && msg.slot < inv.getSize()){
                        ItemStack hand = connection.player.getHandItemStack();
                        connection.player.setHandItemStack(inv.getAt(msg.slot));
                        inv.setAt(msg.slot, hand);
                    }
                }
                if(pMsg instanceof OpenedInventoryActionMessage msg){
                    if(msg.action == 0){
                        connection.player.closeInventory();
                    } else {
                        Inventory inv = connection.player.getOpenedInventory();
                        if(inv != null && inv instanceof IActionProcessingInventory actionProcessingInventory){
                            actionProcessingInventory.onInventoryAction(connection.player, inv, msg);
                        }
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

    public List<World> getWorlds() {
        return Collections.unmodifiableList(worlds);
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

    public ScriptingManager scriptingManager() {
        return scriptingManager;
    }
    public void setScriptingManager(ScriptingManager scriptingManager) {
        this.scriptingManager = scriptingManager;
    }

    public Events getEvents() {
        return this.scriptingManager.getEvents();
    }
}
