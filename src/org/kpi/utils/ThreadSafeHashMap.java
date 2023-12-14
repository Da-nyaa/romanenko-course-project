package org.kpi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeHashMap<K, V> {
    private final int capacity;
    private final List<ReentrantLock> locks;
    private final List<Map<K, V>> segments;

    public ThreadSafeHashMap() {
        this.capacity = 16;
        this.locks = new ArrayList<>(capacity);
        this.segments = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            locks.add(new ReentrantLock());
            segments.add(new HashMap<>());
        }
    }

    private int getSegmentIndex(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int segmentIndex = getSegmentIndex(key);
        ReentrantLock lock = locks.get(segmentIndex);

        lock.lock();
        try {
            segments.get(segmentIndex).put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public V get(K key) {
        int segmentIndex = getSegmentIndex(key);
        ReentrantLock lock = locks.get(segmentIndex);

        lock.lock();
        try {
            return segments.get(segmentIndex).get(key);
        } finally {
            lock.unlock();
        }
    }

    public boolean containsKey(K key) {
        int segmentIndex = getSegmentIndex(key);
        ReentrantLock lock = locks.get(segmentIndex);

        lock.lock();
        try {
            return segments.get(segmentIndex).containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        for (Map<K, V> segment : segments) {
            if (!segment.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}



