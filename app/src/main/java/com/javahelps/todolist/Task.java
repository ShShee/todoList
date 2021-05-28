package com.javahelps.todolist;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String taskName;
    private String taskTime;

    private boolean active;

    public Task(String taskName, String taskTime)  {
        this.taskName= taskName;
        this.taskTime = taskTime;
        this.active= false;
    }

    public Task(int id,String taskName, String taskTime, boolean active)  {
        this.id=id;
        this.taskName= taskName;
        this.taskTime = taskTime;
        this.active= active;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.taskName +" (Time: "+ this.taskTime+")";
    }

}