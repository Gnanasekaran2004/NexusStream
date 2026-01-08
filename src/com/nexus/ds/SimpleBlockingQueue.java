package com.nexus.ds;

import java.util.LinkedList;

public class SimpleBlockingQueue<T> {
    private final LinkedList<T> queue;
    private final int capacity;

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }
    public synchronized void produce(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            System.out.println(Thread.currentThread().getName() + " is waiting (Queue Full).");
            wait();
        }
        
        queue.add(item);
        System.out.println(Thread.currentThread().getName() + " produced: " + item);
        
        notifyAll();
    }
    public synchronized T consume() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " is waiting (Queue Empty).");
            wait();
        }

        T item = queue.removeFirst();
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return queue.size();
    }
}
