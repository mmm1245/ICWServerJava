package com.github.industrialcraft.icwserver.net;

import com.github.industrialcraft.icwserver.GameServer;
import com.github.industrialcraft.icwserver.util.Pair;
import com.github.industrialcraft.icwserver.world.entity.Entity;
import com.google.gson.Gson;
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
    private final List<ClientConnection> connections;
    private final GameServer gameServer;
    private final Queue<Pair<ClientConnection,Message>> messageQueue;
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
        System.out.println("new connection");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Entity player = conn.<ClientConnection>getAttachment().getPlayer();
        if(player != null)
            player.remove();
        this.connections.remove(conn.getAttachment());
        conn.setAttachment(null);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try{
            Message parsedMessage = MessageRegistry.create(JsonParser.parseString(message).getAsJsonObject());
            System.out.println(parsedMessage);  //todo:remove debug
            this.messageQueue.add(new Pair<>(conn.getAttachment(),parsedMessage));
        } catch (Exception ex){
            onError(conn, ex);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if(conn == null){
            ex.printStackTrace();
            return;
        }
        Entity player = conn.<ClientConnection>getAttachment().getPlayer();
        if(player != null)
            player.remove();
    }

    @Override
    public void onStart() {

    }

    public void broadcast(Message message){
        for(ClientConnection connection : this.connections){
            if(connection.getPlayer()!=null)
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
