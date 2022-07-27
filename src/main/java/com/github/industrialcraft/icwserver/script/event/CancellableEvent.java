package com.github.industrialcraft.icwserver.script.event;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;

public class CancellableEvent {
    protected ArrayList<ScriptObjectMirror> registered;
    public CancellableEvent() {
        this.registered = new ArrayList<>();
    }

    public void register(ScriptObjectMirror scriptLambda){
        registered.add(scriptLambda);
    }

    public boolean call(Object... data){
        Cancellable cancellable = new Cancellable();
        for(ScriptObjectMirror scriptObject : registered){
            scriptObject.call(cancellable, data);
        }
        return cancellable.cancelled();
    }

    public static class Cancellable{
        private boolean cancelled;
        public Cancellable() {
            this.cancelled = false;
        }
        public boolean cancelled() {
            return cancelled;
        }
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }
}
