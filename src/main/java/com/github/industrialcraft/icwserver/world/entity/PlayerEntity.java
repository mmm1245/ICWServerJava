package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.messages.ChatMessage;
import com.github.industrialcraft.icwserver.net.messages.ClientPlayerPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.KnockbackDataMessage;
import com.github.industrialcraft.icwserver.net.messages.TeleportPlayerMessage;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.script.JSPlayer;
import com.github.industrialcraft.icwserver.util.EWorldOrientation;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.util.playerState.RunningPlayerState;
import com.github.industrialcraft.icwserver.util.playerState.PlayerState;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class PlayerEntity extends Entity {
    private ClientConnection connection;
    public final PhysicsObject physicsObject;
    private Inventory inventory;
    private Entity openedInventory;
    private ItemStack handItemStack;
    private PlayerAbilities playerAbilities;
    private RunningPlayerState runningPlayerState;
    public PlayerEntity(Location location, ClientConnection connection) {
        super(location);
        this.connection = connection;
        this.physicsObject = new PhysicsObject(this, 4, 20, EPhysicsLayer.OBJECT);

        this.handItemStack = null;
        this.inventory = new Inventory(10, (inventory1, is) -> {
            PlayerEntity pl = inventory1.getData();
            if(!pl.isDead()){
                new ItemStackEntity(pl.getLocation(), is);
            }
        }, this);

        this.playerAbilities = new PlayerAbilities();
        this.runningPlayerState = null;
    }
    @Override
    public void tick() {
        this.getPlayerAbilities().reset();
        this.getPlayerAbilities().topDown = this.getLocation().world().orientation==EWorldOrientation.TOP_DOWN;
        super.tick();
        if(openedInventory != null && (openedInventory.isDead() || (openedInventory.getInventory()==null||openedInventory.getLocation().distanceToNS(getLocation())>50*50)))
            openedInventory = null;

        if(runningPlayerState != null){
            runningPlayerState.next();
            if(runningPlayerState.isFinished())
                runningPlayerState = null;
        }

        getServer().getEvents().PLAYER_TICK.call(new JSPlayer(this));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("name", connection.profile.name());
        json.addProperty("state", runningPlayerState ==null?"default": runningPlayerState.getStringState());
        return json;
    }

    public void sendChatMessage(String text){
        if(connection != null)
            connection.send(new ChatMessage(text));
    }

    public PlayerAbilities getPlayerAbilities() {
        return playerAbilities;
    }

    public void setPlayerState(PlayerState state){
        if(state == null)
            this.runningPlayerState = null;
        else {
            if(this.runningPlayerState == null || this.runningPlayerState.getState() != state)
                this.runningPlayerState = new RunningPlayerState(state);
        }
    }
    public void setPlayerStateWithResetting(PlayerState state){
        if(state == null)
            this.runningPlayerState = null;
        else
            this.runningPlayerState = new RunningPlayerState(state);
    }

    public RunningPlayerState getPlayerState() {
        return runningPlayerState;
    }

    public void openInventory(Entity entity){
        if(entity.getInventory()==null)
            return;
        if(entity instanceof PlayerEntity)
            return;
        this.openedInventory = entity;
    }
    public void closeInventory(){
        this.openedInventory = null;
    }
    public Inventory getOpenedInventory(){
        if(this.openedInventory == null)
            return null;
        return this.openedInventory.getInventory();
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }

    public ItemStack getHandItemStack() {
        if(this.handItemStack != null && this.handItemStack.getCount() <= 0)
            return null;
        return handItemStack;
    }
    public void setHandItemStack(ItemStack handItemStack) {
        this.handItemStack = handItemStack;
    }

    public void setLocationFromClient(ClientPlayerPositionMessage message){
        //todo: anticheat
        this.location = this.location.withXY(message.x, message.y);
    }
    @Override
    public void applyKnockback(float x, float y){
        if(connection != null)
            connection.send(new KnockbackDataMessage(x, y));
    }

    @Override
    public void teleport(Location location) {
        super.teleport(location);
        rubberbandPlayer();
    }
    public void rubberbandPlayer(){
        if(connection != null)
            connection.send(new TeleportPlayerMessage(this));
    }

    @Override
    public String getType() {
        return "player";
    }

    @Override
    public void kill() {
        getServer().getEvents().PLAYER_DEATH.call(new JSPlayer(this));
        inventory.dropAll();
        //todo: drop hand stack
        inventory.overflow(handItemStack);
        handItemStack = null;
        closeInventory();
        getPhysicalObject().resetKnockback();
        teleport(getLocation().world().getServer().getLobby().getSpawn());
        setHealth(getMaxHealth());
    }

    @Override
    public Entity clone(Location newLocation) {
        throw new UnsupportedOperationException("cannot clone player");
    }

    @Override
    public boolean isDead() {
        return connection == null;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public PhysicsObject getPhysicalObject() {
        return this.physicsObject;
    }

    public ClientConnection getConnection() {
        return connection;
    }
    public void removeConnection(){
        this.connection.player = null;
        this.connection.profile = null;
        this.connection = null;
    }
}
