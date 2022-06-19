package com.github.industrialcraft.icwserver.world.entity;

import com.github.industrialcraft.icwserver.net.ClientConnection;
import com.github.industrialcraft.icwserver.net.messages.ClientPlayerPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.TeleportPlayerMessage;
import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.google.gson.JsonObject;

public class PlayerEntity extends DamageableEntity implements IPhysicalEntity{
    private ClientConnection connection;
    public final PhysicsObject physicsObject;
    private Inventory inventory;
    public PlayerEntity(Location location, ClientConnection connection) {
        super(location);
        this.connection = connection;
        this.physicsObject = new PhysicsObject(this, 4, 20, EPhysicsLayer.OBJECT);

        this.inventory = new Inventory(10, (inventory1, is) -> {
            PlayerEntity pl = (PlayerEntity) inventory1.getData();
            if(!pl.isDead()){
                new ItemStackEntity(pl.getLocation(), is);
            }
        }, this);
    }
    @Override
    public void tick() {

    }
    @Override
    public float getMaxHealth() {
        return 100;
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
        teleport(getLocation().world().getServer().getLobby().getSpawn());
    }

    @Override
    public boolean isDead() {
        return connection == null;
    }

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
        this.connection = null;
    }
}
