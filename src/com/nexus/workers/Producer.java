package com.nexus.workers;

import com.nexus.core.Task;
import com.nexus.ds.SimpleBlockingQueue;
import com.nexus.io.AuditLogger;
import java.util.Random;

public class Producer implements Runnable {
    private final SimpleBlockingQueue<Task> queue;
    private final int id;
    private final int taskCount;
    private final Random random = new Random();

    public Producer(int id, SimpleBlockingQueue<Task> queue, int taskCount) {
        this.id = id;
        this.queue = queue;
        this.taskCount = taskCount;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < taskCount; i++) {
                // Simulate some work to generate a task
                Thread.sleep(random.nextInt(500));

                int priority = random.nextInt(10) + 1; // 1-10
                Task task = new Task(
                        (id * 1000) + i,
                        "Data Payload from Producer " + id,
                        priority);

                queue.produce(task);
                AuditLogger.log("PRODUCED: " + task);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
