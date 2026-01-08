package com.nexus.core;

public record Task(int id, String description, int priority) {
    public Task {
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Priority must be between 1 and 10");
        }
    }

    @Override
    public String toString() {
        return "Task[#" + id + " | Pri:" + priority + "] " + description;
    }
}
