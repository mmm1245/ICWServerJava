package com.github.industrialcraft.icwserver.script;

import javax.script.*;
import java.io.File;
import java.io.FileReader;

public class ScriptingManager {
    private ScriptEngineManager manager;
    private ScriptEngine engine;
    private Bindings binding;

    private JSItemRegistry itemRegistry;
    private JSGameServer gameServer;
    public ScriptingManager(JSGameServer gameServer, boolean debugEnabled) {
        this.gameServer = gameServer;
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("JavaScript");

        this.binding = this.engine.createBindings();

        this.itemRegistry = new JSItemRegistry();
        this.binding.put("itemRegistry", this.itemRegistry);
        this.binding.put("items", this.itemRegistry.getItems());

        this.binding.put("log", new JSLogger(debugEnabled));

        this.binding.put("gameServer", this.gameServer);

        this.engine.setBindings(this.binding, ScriptContext.GLOBAL_SCOPE);
    }
    public void runInitScript(File... scripts){
        for(File file : scripts){
            try {
                FileReader reader = new FileReader(file);
                engine.eval(reader);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
