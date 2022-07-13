package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.inventory.Item;
import com.github.industrialcraft.icwserver.inventory.data.IActionProcessingInventory;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.WSServer;
import com.github.industrialcraft.icwserver.net.messages.*;
import com.github.industrialcraft.icwserver.physics.Raytracer;
import com.github.industrialcraft.icwserver.script.JSGameServer;
import com.github.industrialcraft.icwserver.script.JSWorld;
import com.github.industrialcraft.icwserver.script.ScriptingManager;
import com.github.industrialcraft.icwserver.script.event.Events;
import com.github.industrialcraft.icwserver.util.*;
import com.github.industrialcraft.icwserver.world.Particle;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.*;
import com.github.industrialcraft.icwserver.world.entity.craftingStation.WoodWorkingStation;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;

import java.io.File;
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
    protected long ticksLasted;
    protected CommandManager commandManager;
    protected final TickManager tickManager;
    public GameServer(InetSocketAddress address, File[] files) {
        this.server = new WSServer(address, this);
        this.server.setReuseAddr(true);
        this.worlds = new ArrayList<>();
        this.entityIdGenerator = 0;
        this.worldIdGenerator = 0;
        this.ticksLasted = 0;
        this.scriptingManager = new ScriptingManager(new JSGameServer(this), true);
        this.scriptingManager.runScripts(files);
        this.worlds.add(new World(true, this));
        getEvents().CREATE_WORLD.call(new JSWorld(this.worlds.get(0)));

        new PlatformEntity(new Location(0, -30, worlds.get(0)), 100, 5);
        new WoodWorkingStation(new Location(50, 0, worlds.get(0)));

        getEvents().START_SERVER.call();
        this.commandManager = new CommandManager();
        this.tickManager = new TickManager();
    }
    public TickManager getTickManager() {
        return tickManager;
    }
    public World createWorld(){
        World world = new World(false, this);
        this.worlds.add(world);
        getEvents().CREATE_WORLD.call(new JSWorld(world));
        return world;
    }

    public WSServer getWSServer() {
        return server;
    }

    public Entity entityById(int id){
        for(World world : worlds){
            for(Entity entity : world.getEntities()){
                if(entity.id == id)
                    return entity;
            }
        }
        return null;
    }

    public PlayerEntity playerByName(String name){
        for(ClientConnection connection : getWSServer().getClientConnections()){
            if(connection.player == null || connection.profile == null)
                continue;
            if(connection.profile.name().equals(name)){
                return connection.player;
            }
        }
        return null;
    }

    @Override
    public void run() {
        server.start();
        while(true) {
            this.worlds.removeIf(world -> world.isRemoved());

            if(tickManager.shouldTick()) {
                getEvents().SERVER_TICK.call();
                for (World world : this.worlds) {
                    getEvents().WORLD_TICK.call(world);
                    world.tick();
                }
            } else {
                for (World world : this.worlds) {
                    world.partialTick();
                }
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
                    if(!Util.isPlayerNameValid(msg.username)){
                        connection.disconnect("invalid username");
                        continue;
                    }
                    if(playerByName(msg.username) != null){
                        connection.disconnect("username taken");
                        continue;
                    }
                    PlayerEntity pl = new PlayerEntity(getLobby().getSpawn(), connection);
                    connection.profile = new RPlayerProfile(msg.username, UUID.randomUUID());
                    connection.player = pl;
                    connection.send(new ControllingEntityMessage(pl));
                    getEvents().PLAYER_JOIN.call(pl);
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
                        connection.player.getLocation().world().addParticle(new Particle("fist", 5, connection.player.getLocation()).addNumber("angle", msg.angle));
                        Entity entity = Raytracer.raytrace(connection.player.getLocation().addXY(2, 15), msg.angle, 50, ent -> ent.id != connection.player.id);
                        if(entity != null)
                            entity.damage(-15, EDamageType.FIST);
                    }
                }
                if(pMsg instanceof InteractEntityMessage msg){
                    Entity entity = connection.player.getLocation().world().byId(msg.entityId);
                    if(entity != null){
                        entity.onPlayerInteract(connection.player, msg);
                    }
                }
                if(pMsg instanceof PlayerClickSlotMessage msg){
                    if (msg.slot == -1 && msg.secondInventory == false && connection.player.getHandItemStack() != null) {
                        new ItemStackEntity(connection.player.getLocation(), connection.player.getHandItemStack().clone());
                        connection.player.setHandItemStack(null);
                        continue;
                    }
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
                if(pMsg instanceof CustomDataMessage msg){
                    getEvents().CUSTOM_MESSAGE_RECEIVED.call(msg.type, msg.data);
                }
                if(pMsg instanceof ChatMessage msg){
                    if(msg.text.isEmpty())
                        continue;
                    if(msg.text.charAt(0) == '/'){
                        commandManager.execute(connection.player, msg.text.substring(1));
                    } else {
                        //todo: message event
                        server.broadcast(new ChatMessage(String.format("<%s>%s", connection.profile.name(), msg.text)));
                    }
                }
            }
            if(tickManager.shouldTick())
                this.ticksLasted++;

            if(tickManager.getWarpTime() > 0){
                tickManager.decreaseWarpTime();
                if(tickManager.getWarpTime() == 0)
                    getWSServer().broadcast(new ChatMessage("Tick warp complete"));
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    public long ticksLasted() {
        return ticksLasted;
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

    public ScriptingManager getScriptingManager() {
        return scriptingManager;
    }

    public Events getEvents() {
        return this.scriptingManager.events;
    }
}
