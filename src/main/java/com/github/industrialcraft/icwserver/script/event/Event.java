package com.github.industrialcraft.icwserver.script.event;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;

public class Event {
    protected ArrayList<ScriptObjectMirror> registered;
    public Event() {
        this.registered = new ArrayList<>();
    }
    public void register(ScriptObjectMirror scriptLambda){
        registered.add(scriptLambda);
    }
    public void call(Object... data){
        for(ScriptObjectMirror scriptObject : registered){
            scriptObject.call(null, data);
        }
    }
}
