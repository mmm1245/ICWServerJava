package com.github.industrialcraft.icwserver.util.taunt;

public class RunningTaunt {
    private final Taunt taunt;
    private int part;
    private int timeLeft;
    private boolean finished;
    public RunningTaunt(Taunt taunt) {
        this.taunt = taunt;
        this.part = 0;
        this.timeLeft = taunt.parts.get(0).length();
        this.finished = false;
    }
    public void next(){
        if(this.finished)
            return;
        timeLeft--;
        if(timeLeft <= 0){
            part++;
            if(part >= taunt.parts.size()){
                finished = true;
                return;
            }
            timeLeft = taunt.parts.get(part).length();
        }
    }
    public String getState(){
        return taunt.id + "_" + part;
    }
    public boolean isFinished() {
        return finished;
    }
}
