package com.github.industrialcraft.icwserver.net;

import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

public class ClientConnection {
    private final WebSocket conn;
    private Entity player;
    public ClientConnection(WebSocket conn) {
        this.conn = conn;
    }
    public void disconnect(){
        conn.close(CloseFrame.NORMAL);
    }
    public void disconnect(String reason){
        conn.close(CloseFrame.NORMAL, reason);
    }
    public void send(Message message){
        JsonObject json = message.toJson();
        json.addProperty("type", message.getType());
        conn.send(json.toString());
    }

    public WebSocket getConn() {
        return conn;
    }
    public Entity getPlayer() {
        return player;
    }
    public void setPlayer(Entity player) {
        this.player = player;
    }
}
