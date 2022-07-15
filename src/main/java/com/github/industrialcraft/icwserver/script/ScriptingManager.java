package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.script.event.Events;
import org.openjdk.nashorn.api.scripting.NashornScriptEngine;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.openjdk.nashorn.internal.objects.Global;
import org.openjdk.nashorn.internal.parser.JSONParser;
import org.openjdk.nashorn.internal.runtime.Context;
import org.openjdk.nashorn.internal.runtime.JSONFunctions;

import javax.script.*;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ScriptingManager {
    private NashornScriptEngineFactory manager;
    private ScriptEngine engine;
    private Bindings binding;

    public final JSItemRegistry itemRegistry;
    public final JSEntityRegistry entityRegistry;
    public final JSGameServer gameServer;
    public final Events events;
    public ScriptingManager(JSGameServer gameServer, boolean debugEnabled)  {
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

        this.binding.put("EPhysicsLayers", Arrays.stream(EPhysicsLayer.values()).collect(Collectors.toUnmodifiableMap(o -> o.name(), o -> o)));

        this.engine.setBindings(this.binding, ScriptContext.GLOBAL_SCOPE);
    }
    public ScriptEngine getEngine() {
        return engine;
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
