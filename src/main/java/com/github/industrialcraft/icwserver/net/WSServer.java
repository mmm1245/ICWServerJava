package com.github.industrialcraft.icwserver.net;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.net.messages.AssetDataMessage;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.World;
import com.github.industrialcraft.icwserver.world.entity.PlayerEntity;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WSServer extends WebSocketServer
{
    protected final List<ClientConnection> connections;
    protected final GameServer gameServer;
    protected final Queue<Pair<ClientConnection,Message>> messageQueue;
    public WSServer(InetSocketAddress address, GameServer gameServer) {
        super(address);
        this.connections = new ArrayList<>();
        this.gameServer = gameServer;
        this.messageQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        ClientConnection clientConnection = new ClientConnection(conn);
        conn.setAttachment(clientConnection);
        this.connections.add(clientConnection);
        clientConnection.send(new AssetDataMessage(gameServer.getScriptingManager().entityRegistry, gameServer.getScriptingManager().itemRegistry));
        System.out.println("new connection");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        PlayerEntity player = conn.<ClientConnection>getAttachment().player;
        if(player != null) {
            player.getServer().getEvents().PLAYER_LEAVE.call(player);
            player.removeConnection();
        }
        this.connections.remove(conn.getAttachment());
        conn.setAttachment(null);
        System.out.println("disconnect: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try{
            Message parsedMessage = MessageRegistry.create(JsonParser.parseString(message).getAsJsonObject());
            this.messageQueue.add(new Pair<>(conn.getAttachment(),parsedMessage));
        } catch (Exception ex){
            onError(conn, ex);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if(conn == null){
            return;
        }
        PlayerEntity player = conn.<ClientConnection>getAttachment().player;
        if(player != null)
            player.removeConnection();
        conn.close();
    }

    @Override
    public void onStart() {

    }

    public void broadcast(Message message){
        for(ClientConnection connection : this.connections){
            if(connection.player!=null)
                connection.send(message);
        }
    }
    public void broadcastInWorld(Message message, World world){
        for(ClientConnection connection : this.connections){
            if(connection.player!=null && connection.player.getLocation().world()==world)
                connection.send(message);
        }
    }

    public List<ClientConnection> getClientConnections() {
        return Collections.unmodifiableList(this.connections);
    }
    public boolean hasMessage() {
        return this.messageQueue.peek() != null;
    }
    public Pair<ClientConnection, Message> pollMessage() {
        return this.messageQueue.poll();
    }
}
