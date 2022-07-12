package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.script.JSGameServer;
import com.github.industrialcraft.icwserver.script.ScriptingManager;

import java.io.File;
import java.net.InetSocketAddress;

public class ICWServerMain {
    public static void main(String args[]){
        GameServer gameServer = new GameServer(new InetSocketAddress(5555));
        ScriptingManager scriptingManager = new ScriptingManager(new JSGameServer(gameServer), true);
        gameServer.setScriptingManager(scriptingManager);
        scriptingManager.runInitScript(new File("start.js"));
        gameServer.init();
        gameServer.run();
    }
}
