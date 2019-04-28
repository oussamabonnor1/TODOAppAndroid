package com.miamme.jetlightstudio.foodapp.Model;

public class TodoItem {
    String taskName;
    boolean status;

    public TodoItem(String taskName, boolean status) {
        this.taskName = taskName;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
