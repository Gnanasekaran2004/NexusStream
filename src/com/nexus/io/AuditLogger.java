package com.nexus.io;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
public class AuditLogger {
    private static final String LOG_FILE = "nexus_audit.log";
    private static final Object lock = new Object();
    public static void log(String message) {
        String timestamp = LocalDateTime.now().toString();
        String logEntry = String.format("[%s] %s%n", timestamp, message);
        synchronized (lock) {
            try {
                Path path = Paths.get(LOG_FILE);
                Files.write(path, logEntry.getBytes(), 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.println("Failed to write to audit log: " + e.getMessage());
            }
        }
    }
}
