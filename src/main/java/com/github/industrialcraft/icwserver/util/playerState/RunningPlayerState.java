package com.github.industrialcraft.icwserver.util.playerState;

public class RunningPlayerState {
    private final PlayerState playerState;
    private int part;
    private int timeLeft;
    private boolean finished;
    public RunningPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        this.part = 0;
        this.timeLeft = playerState.parts.get(0).length();
        this.finished = false;
    }
    public void next(){
        if(this.finished)
            return;
        timeLeft--;
        if(timeLeft <= 0){
            part++;
            if(part >= playerState.parts.size()){
                finished = true;
                return;
            }
            timeLeft = playerState.parts.get(part).length();
        }
    }
    public int getPart() {
        return part;
    }
    public int getTimeLeft() {
        return timeLeft;
    }
    public String getStringState(){
        return playerState.id + "_" + part;
    }
    public boolean isFinished() {
        return finished;
    }
    public PlayerState getState(){
        return playerState;
    }
}
