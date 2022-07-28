package com.github.industrialcraft.icwserver.script;

import com.github.industrialcraft.icwserver.physics.EPhysicsLayer;
import com.github.industrialcraft.icwserver.script.event.Events;
import com.github.industrialcraft.icwserver.util.CommandManager;
import com.github.industrialcraft.icwserver.util.EWorldOrientation;
import com.github.industrialcraft.icwserver.world.entity.data.EDamageType;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.openjdk.nashorn.internal.runtime.ECMAException;

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
    public final JSPlayerStateRegistry playerStateRegistry;
    public final JSStatusEffectRegistry statusEffectRegistry;
    public final JSSoundEffectRegistry soundEffectRegistry;
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

        this.playerStateRegistry = new JSPlayerStateRegistry();
        this.binding.put("playerStateRegistry", this.playerStateRegistry);
        this.binding.put("playerStates", this.playerStateRegistry.getPlayerStates());

        this.statusEffectRegistry = new JSStatusEffectRegistry();
        this.binding.put("statusEffectRegistry", this.statusEffectRegistry);
        this.binding.put("statusEffects", this.statusEffectRegistry.getStatusEffects());

        this.soundEffectRegistry = new JSSoundEffectRegistry();
        this.binding.put("soundEffectRegistry", this.soundEffectRegistry);
        this.binding.put("soundEffects", this.soundEffectRegistry.getSoundEffects());

        this.binding.put("log", new JSLogger(debugEnabled));

        this.binding.put("gameServer", this.gameServer);

        this.binding.put("events", this.events);

        this.binding.put("scheduler", this.gameServer.getInternal().getScheduler());

        this.binding.put("EDamageType", Arrays.stream(EDamageType.values()).collect(Collectors.toUnmodifiableMap(o -> o.name(), o -> o)));

        this.binding.put("EWorldOrientation", Arrays.stream(EWorldOrientation.values()).collect(Collectors.toUnmodifiableMap(o -> o.name(), o -> o)));

        this.binding.put("EPhysicsLayers", Arrays.stream(EPhysicsLayer.values()).collect(Collectors.toUnmodifiableMap(o -> o.name(), o -> o)));

        this.binding.put("commandManager", gameServer.getInternal().getCommandManager());
        this.binding.put("ECommandArgumentType", Arrays.stream(CommandManager.EArgumentType.values()).collect(Collectors.toUnmodifiableMap(o -> o.name(), o -> o)));

        this.engine.setBindings(this.binding, ScriptContext.GLOBAL_SCOPE);

        try {
            JSUtilFunctions.defineObjectAssign(this.engine);
            JSUtilFunctions.defineEntityDataMerge(this.engine);
            JSUtilFunctions.defineDeepCopy(this.engine);
            JSUtilFunctions.defineJsonToString(this.engine);
        } catch (ScriptException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    public ScriptEngine getEngine() {
        return engine;
    }
    public Invocable getInvocable() {
        return (Invocable) engine;
    }
    public Object tryDeepCopy(Object data){
        try {
            return getInvocable().invokeFunction("deepCopyObject", data);
        } catch (Exception e) {
            return null;
        }
    }
    public String tryJsonToString(Object data){
        try {
            return getInvocable().invokeFunction("jsonToString", data).toString();
        } catch (Exception e) {
            return null;
        }
    }
    public void runScripts(File... scripts){
        for(File file : scripts){
            try {
                FileReader reader = new FileReader(file);
                engine.eval(reader);
                reader.close();
            } catch (Exception e) {
                System.out.println("Exception in: " + file.getName());
                e.printStackTrace();
            }
        }
    }
}
