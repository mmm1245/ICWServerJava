package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.inventory.data.IInventoryHolder;
import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.messages.ClientPlayerPositionMessage;
import com.github.industrialcraft.icwserver.net.messages.TeleportPlayerMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.util.Location;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;
import com.google.gson.JsonObject;
import org.java_websocket.framing.CloseFrame;

public class JSPlayer {
    PlayerEntity player;
    public JSPlayer(PlayerEntity player) {
        this.player = player;
    }
    public void openInventory(IInventoryHolder holder){
        this.player.openInventory(holder);
    }
    public void closeInventory(){
        this.player.closeInventory();
    }
    public Inventory getOpenedInventory(){
        return this.player.getOpenedInventory();
    }
    public float getMaxHealth() {
        return player.getMaxHealth();
    }

    public ItemStack getHandItemStack() {
        return this.player.getHandItemStack();
    }
    public void setHandItemStack(ItemStack handItemStack) {
        this.player.setHandItemStack(handItemStack);
    }
    public void teleport(JSLocation location) {
        this.player.teleport(location.getInternal());
    }
    public void kill() {
        this.player.kill();
    }
    public boolean isDisconnected() {
        return player.isDead();
    }
    public Inventory getInventory() {
        return player.getInventory();
    }
    public PhysicsObject getPhysicalObject() {
        return this.player.getPhysicalObject();
    }
    public void disconnect(){
        this.player.getConnection().disconnect();
    }
    public void disconnect(String reason){
        this.player.getConnection().disconnect(reason);
    }
    public void sendMessage(Message message){
        player.getConnection().send(message);
    }
}
