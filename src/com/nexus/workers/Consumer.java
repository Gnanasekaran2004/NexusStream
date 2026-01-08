package com.nexus.workers;

import com.nexus.core.Task;
import com.nexus.ds.SimpleBlockingQueue;
import com.nexus.interfaces.ProcessingStrategy;
import com.nexus.io.AuditLogger;
import com.nexus.util.StatsManager;

public class Consumer implements Runnable {
    private final SimpleBlockingQueue<Task> queue;
    private final int id;
    private final ProcessingStrategy strategy;
    private volatile boolean running = true;

    public Consumer(int id, SimpleBlockingQueue<Task> queue, ProcessingStrategy strategy) {
        this.id = id;
        this.queue = queue;
        this.strategy = strategy;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Task task = queue.consume();

                AuditLogger.log("CONSUMER " + id + " STARTING: " + task);
                strategy.process(task);
                AuditLogger.log("CONSUMER " + id + " COMPLETED: " + task);

                StatsManager.recordTaskCompletion(task);
            }
        } catch (InterruptedException e) {
            System.out.println("Consumer " + id + " stopping.");
            Thread.currentThread().interrupt();
        }
    }
}
