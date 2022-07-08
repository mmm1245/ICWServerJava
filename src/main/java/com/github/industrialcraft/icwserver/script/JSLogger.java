package com.github.industrialcraft.icwserver.script;

public class JSLogger {
    boolean debugEnabled;
    public JSLogger(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public void info(Object data){
        System.out.println("[INFO] " + data.toString());
    }
    public void debug(Object data){
        if(debugEnabled)
            System.out.println("[DEBUG] " + data.toString());
    }
    public void warn(Object data){
        System.out.println("[WARN] " + data.toString());
    }
    public void error(Object data){
        System.out.println("[ERROR] " + data.toString());
    }
    public void fatal(Object data){
        System.out.println("[FATAL] " + data.toString());
        System.exit(0);
    }
}
