package com.nexus;

import com.nexus.core.Task;
import com.nexus.ds.SimpleBlockingQueue;
import com.nexus.interfaces.ProcessingStrategy;
import com.nexus.io.AuditLogger;
import com.nexus.util.StatsManager;
import com.nexus.workers.Consumer;
import com.nexus.workers.Producer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting NexusStream Application...");
        AuditLogger.log("Application Started");

        SimpleBlockingQueue<Task> taskQueue = new SimpleBlockingQueue<>(5);

        ProcessingStrategy fastStrategy = (task) -> {
            int sleepTime = (11 - task.priority()) * 100;
            Thread.sleep(sleepTime);
            System.out.println(" processed task: " + task.id() + " [Pri: " + task.priority() + "]");
        };

        int producerCount = 2;
        int consumerCount = 3;
        int tasksPerProducer = 5;

        List<Thread> producers = new ArrayList<>();
        List<Thread> consumers = new ArrayList<>();
        List<Consumer> consumerObjects = new ArrayList<>();

        for (int i = 0; i < producerCount; i++) {
            Producer p = new Producer(i + 1, taskQueue, tasksPerProducer);
            Thread t = new Thread(p, "Producer-" + (i + 1));
            producers.add(t);
            t.start();
        }

        for (int i = 0; i < consumerCount; i++) {
            Consumer c = new Consumer(i + 1, taskQueue, fastStrategy);
            consumerObjects.add(c);
            Thread t = new Thread(c, "Consumer-" + (i + 1));
            consumers.add(t);
            t.start();
        }
        for (Thread t : producers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All Producers finished.");
        AuditLogger.log("All Producers finished");

        while (taskQueue.size() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Consumers...");
        for (Consumer c : consumerObjects) {
            c.stop();
        }
        for (Thread t : consumers) {
            t.interrupt();
        }
        StatsManager.printReport();
        AuditLogger.log("Application Finished");
        System.out.println("NexusStream Completed.");
    }
}
