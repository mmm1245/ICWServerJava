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
        if(time < 0)
            throw new IllegalArgumentException("time cannot be lower than 0");
        int taskId = taskIdGenerator++;
        this.scheduledTasks.add(new ScheduledTask(taskId,time, 0, -1, task, data));
        return taskId;
    }
    public int repeating(ScriptObjectMirror task, int time, int repeatTime, Object data){
        if(time < 0)
            throw new IllegalArgumentException("time cannot be lower than 0");
        if(repeatTime <= 0)
            throw new IllegalArgumentException("repeat time cannot be lower or equal to 0");
        int taskId = taskIdGenerator++;
        this.scheduledTasks.add(new ScheduledTask(taskId,time, repeatTime, -1, task, data));
        return taskId;
    }
    public int times(ScriptObjectMirror task, int time, int repeatTime, int repeatNum, Object data){
        if(time < 0)
            throw new IllegalArgumentException("time cannot be lower than 0");
        if(repeatTime <= 0)
            throw new IllegalArgumentException("repeat time cannot be lower or equal to 0");
        if(repeatNum <= 0)
            throw new IllegalArgumentException("repeat num cannot be lower or equal to 0");
        int taskId = taskIdGenerator++;
        this.scheduledTasks.add(new ScheduledTask(taskId,time, repeatTime, repeatNum-1, task, data));
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
                if(task.repeatTime > 0) {
                    if(task.repeatNum == -1){
                        task.timeLeft = task.repeatTime;
                    } else {
                        if (task.repeatNum > 0){
                            task.repeatNum--;
                            task.timeLeft = task.repeatTime;
                        }
                    }
                }
            }
        }
        scheduledTasks.removeIf(scheduledTask -> scheduledTask.timeLeft <= 0);
    }

    private class ScheduledTask {
        private final int id;
        private int timeLeft;
        private int repeatTime;
        private int repeatNum;
        private ScriptObjectMirror task;
        private Object data;
        public ScheduledTask(int id, int timeLeft, int repeatTime, int repeatNum, ScriptObjectMirror task, Object data) {
            this.id = id;
            this.timeLeft = timeLeft;
            this.repeatTime = repeatTime;
            this.repeatNum = repeatNum;
            this.task = task;
            this.data = data;
        }
    }
}
