package com.github.industrialcraft.icwserver.net;

import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.github.industrialcraft.icwserver.world.entity.PlayerProfile;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

public class ClientConnection {
    protected final WebSocket conn;
    public PlayerEntity player;
    public PlayerProfile profile;
    public ClientConnection(WebSocket conn) {
        this.conn = conn;
    }
    public void disconnect(){
        if(!conn.isClosed())
            conn.close(CloseFrame.NORMAL);
    }
    public void disconnect(String reason){
        if(!conn.isClosed())
            conn.close(CloseFrame.NORMAL, reason);
    }
    public void send(Message message){
        JsonObject json = message.toJson();
        json.addProperty("type", message.getType());
        if(!conn.isClosed())
            conn.send(json.toString());
    }

    public WebSocket getConn() {
        return conn;
    }
}
