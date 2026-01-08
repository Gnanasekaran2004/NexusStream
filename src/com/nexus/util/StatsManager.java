package com.nexus.util;

import com.nexus.core.Task;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatsManager {
    private static final Map<Integer, Integer> priorityStats = new ConcurrentHashMap<>();

    public static void recordTaskCompletion(Task task) {
        priorityStats.merge(task.priority(), 1, Integer::sum);
    }
    public static void printReport() {
        System.out.println("\n NexusStream Execution Report ");
        System.out.println("Tasks processed by Priority:");
        priorityStats.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(
                        entry -> System.out.println("Priority " + entry.getKey() + ": " + entry.getValue() + " tasks"));

        int total = priorityStats.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total Tasks Processed: " + total);
        System.out.println("\n");
    }
}
