package com.nexus.interfaces;

import com.nexus.core.Task;


@FunctionalInterface
public interface ProcessingStrategy {
    void process(Task task) throws InterruptedException;
}
