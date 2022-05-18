package com.github.industrialcraft.icwserver;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ICWServerMain {
    public static void main(String args[]){
        GameServer gameServer = new GameServer(new InetSocketAddress(5555));
        gameServer.run();
    }
}
