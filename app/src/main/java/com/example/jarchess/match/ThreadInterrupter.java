package com.example.jarchess.match;

import java.util.Collection;
import java.util.LinkedList;

public class ThreadInterrupter {
    final Collection<Thread> c;

    public ThreadInterrupter() {
        this(new LinkedList<Thread>());
    }

    public ThreadInterrupter(Collection<Thread> collection) {
        this.c = collection;
    }

    public synchronized boolean add(Thread tread) {
        return c.add(tread);
    }

    public synchronized void interruptAll() {
        for (Thread t : c) {
            t.interrupt();
        }
    }

    public boolean remove(Thread thread) {
        return c.remove(thread);
    }
}
