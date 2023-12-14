package org.kpi.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashMapTest {
    public static void main(String[] args) {
        final int THREAD_COUNT = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        ThreadSafeHashMap<String, String> hashTable = new ThreadSafeHashMap<>();

        try {
            for (int i = 0; i < THREAD_COUNT; i++) {
                final int threadNumber = i;
                executorService.submit(() -> performOperations(hashTable, threadNumber));
            }
        } finally {
            executorService.shutdown();
        }
    }

    private static void performOperations(ThreadSafeHashMap<String, String> hashTable, int threadNumber) {
        for (int i = 0; i < 3; i++) {
            String key = "key" + i;
            String value = "Thread" + threadNumber + "_Value" + i;

            hashTable.put(key, value);

            String retrievedValue = hashTable.get(key);
            System.out.println("Thread" + threadNumber + ": Key " + key + ", Retrieved Value " + retrievedValue);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
