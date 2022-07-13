package com.github.industrialcraft.icwserver;

import com.github.industrialcraft.icwserver.script.JSGameServer;
import com.github.industrialcraft.icwserver.script.ScriptingManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ICWServerMain {
    public static void main(String args[]) throws IOException {
        GameServer gameServer = new GameServer(new InetSocketAddress(5555), Files.find(Paths.get("scripts/"), 999, (path, bfa) -> bfa.isRegularFile() && path.toAbsolutePath().toString().endsWith(".js")).map(path -> path.toFile()).toArray(File[]::new));
        gameServer.run();
    }
}
