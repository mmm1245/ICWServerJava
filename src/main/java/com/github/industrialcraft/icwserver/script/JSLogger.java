package com.github.industrialcraft.icwserver.script;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class JSLogger {
    boolean debugEnabled;
    public JSLogger(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public void info(Object text, Object... data){
        System.out.println("[INFO] " + String.format(text.toString(),data));
    }
    public void debug(Object text, Object... data){
        if(debugEnabled)
            System.out.println("[DEBUG] " + String.format(text.toString(),data));
    }
    public void warn(Object text, Object... data){
        System.out.println("[WARN] " + String.format(text.toString(),data));
    }
    public void error(Object text, Object... data){
        System.out.println("[ERROR] " + String.format(text.toString(),data));
    }
    public void fatal(Object text, Object... data){
        System.out.println("[FATAL] " + String.format(text.toString(),data));
        System.exit(0);
    }
}
