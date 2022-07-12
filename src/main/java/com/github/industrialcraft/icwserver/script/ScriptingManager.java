package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.script.event.Events;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.*;
import java.io.File;
import java.io.FileReader;

public class ScriptingManager {
    private NashornScriptEngineFactory manager;
    private ScriptEngine engine;
    private Bindings binding;

    public final JSItemRegistry itemRegistry;
    public final JSEntityRegistry entityRegistry;
    public final JSGameServer gameServer;
    public final Events events;
    public ScriptingManager(JSGameServer gameServer, boolean debugEnabled) {
        this.gameServer = gameServer;
        this.manager = new NashornScriptEngineFactory();
        this.engine = manager.getScriptEngine(className -> false);
        this.events = new Events();
        this.binding = this.engine.createBindings();

        this.itemRegistry = new JSItemRegistry();
        this.binding.put("itemRegistry", this.itemRegistry);
        this.binding.put("items", this.itemRegistry.getItems());

        this.entityRegistry = new JSEntityRegistry();
        this.binding.put("entityRegistry", this.entityRegistry);
        this.binding.put("entities", this.entityRegistry.getEntities());

        this.binding.put("log", new JSLogger(debugEnabled));

        this.binding.put("gameServer", this.gameServer);

        this.binding.put("events", this.events);

        this.engine.setBindings(this.binding, ScriptContext.GLOBAL_SCOPE);
    }
    public void runScripts(File... scripts){
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
