package com.github.industrialcraft.icwserver;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<ScheduledTask> scheduledTasks;
    private ArrayList<Integer> toRemove;
    private int taskIdGenerator;
    public Scheduler() {
        this.scheduledTasks = new ArrayList<>();
        this.toRemove = new ArrayList<>();
        this.taskIdGenerator = 0;
    }
    public int schedule(ScriptObjectMirror task, int time, Object data){
        int taskId = taskIdGenerator++;
        this.scheduledTasks.add(new ScheduledTask(taskId,time, task, data));
        return taskId;
    }
    public void killTask(int id){
        toRemove.add(id);
    }
    public void tick(){
        for(Integer id : toRemove){
            scheduledTasks.removeIf(scheduledTask -> scheduledTask.id==id);
        }
        toRemove.clear();

        for(ScheduledTask task : scheduledTasks){
            task.timeLeft--;
            if(task.timeLeft <= 0){
                task.task.call(task.data);
            }
        }
        scheduledTasks.removeIf(scheduledTask -> scheduledTask.timeLeft <= 0);
    }

    private class ScheduledTask {
        private final int id;
        private int timeLeft;
        private ScriptObjectMirror task;
        private Object data;
        public ScheduledTask(int id, int timeLeft, ScriptObjectMirror task, Object data) {
            this.id = id;
            this.timeLeft = timeLeft;
            this.task = task;
            this.data = data;
        }
    }
}
