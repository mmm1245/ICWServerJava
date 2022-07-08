package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.inventory.data.IInventoryHolder;
import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.messages.ClientPlayerPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.TeleportPlayerMessage;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;

public class PlayerEntity extends DamageableEntity implements IPhysicalEntity, IInventoryHolder {
    private ClientConnection connection;
    public final PhysicsObject physicsObject;
    private Inventory inventory;
    private Inventory openedInventory;
    private ItemStack handItemStack;
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
    }
    @Override
    public void tick() {
        if(openedInventory != null && openedInventory.<IInventoryHolder>getData().isInvalidForPlayer(this))
            openedInventory = null;
    }

    public void openInventory(IInventoryHolder holder){
        this.openedInventory = holder.getInventory();
    }
    public void closeInventory(){
        this.openedInventory = null;
    }
    public Inventory getOpenedInventory(){
        return this.openedInventory;
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

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("name", connection.profile.name());
        return json;
    }

    public void setLocationFromClient(ClientPlayerPositionMessage message){
        //todo: anticheat
        this.location = this.location.withXY(message.x, message.y);
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
        inventory.dropAll();
        closeInventory();
        teleport(getLocation().world().getServer().getLobby().getSpawn());
        setHealth(getMaxHealth());
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
    public boolean isInvalidForPlayer(PlayerEntity player) {
        return false;
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
        this.connection = null;
    }
}
