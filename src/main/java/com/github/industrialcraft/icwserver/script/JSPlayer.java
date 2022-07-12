package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.net.Message;
import com.github.industrialcraft.icwserver.net.messages.CustomDataMessage;
import com.github.industrialcraft.icwserver.physics.PhysicsObject;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.inventorysystem.Inventory;
import com.github.industrialcraft.inventorysystem.ItemStack;

public class JSPlayer {
    PlayerEntity player;
    public JSPlayer(PlayerEntity player) {
        this.player = player;
    }
    public void openInventory(Entity entity){
        this.player.openInventory(entity);
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
    public void sendCustomMessage(String type){
        player.getConnection().send(new CustomDataMessage(type, null));
    }
    public void sendCustomMessage(String type, String data){
        player.getConnection().send(new CustomDataMessage(type, data));
    }
}
