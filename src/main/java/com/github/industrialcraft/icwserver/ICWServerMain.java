package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.script.JSGameServer;
import com.github.industrialcraft.icwserver.script.ScriptingManager;

import java.io.File;
import java.net.InetSocketAddress;

public class ICWServerMain {
    public static void main(String args[]){
        GameServer gameServer = new GameServer(new InetSocketAddress(5555));
        //gameServer.run();

        ScriptingManager scriptingManager = new ScriptingManager(new JSGameServer(gameServer), true);
        scriptingManager.runInitScript(new File("start.js"));
        System.out.println(scriptingManager);
    }
}
