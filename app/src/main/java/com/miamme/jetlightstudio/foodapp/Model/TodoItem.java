package com.miamme.jetlightstudio.foodapp.Model;

public class TodoItem {
    String taskName;
    boolean status;
    String color;

    public TodoItem(String taskName, boolean status, String color) {
        this.taskName = taskName;
        this.status = status;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
