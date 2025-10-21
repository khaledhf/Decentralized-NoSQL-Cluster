package org.project.locks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lock {
    private final Map<String, ReadWriteLock> locks = new HashMap<>();
    private static volatile Lock INSTANCE;

    private Lock() {
    }

    public static Lock getInstance() {
        if (INSTANCE == null) {
            synchronized (Lock.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Lock();
                }
            }
        }
        return INSTANCE;
    }
    public ReadWriteLock getLock(String id) {
        if (locks.containsKey(id))
            return locks.get(id);
        else {
            locks.put(id, new ReentrantReadWriteLock());
            return locks.get(id);
        }
    }
    public void deleteLock(String id) {
        locks.remove(id);
    }

}
