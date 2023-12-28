package org.kpi.index;

import org.kpi.utils.ThreadSafeHashMap;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class Index {
    private static ThreadSafeHashMap<String, Set<String>> index;
    private static Index instance;

    private Index() {
        index = new ThreadSafeHashMap<>();
    }

    public static Index getInstance(){
        if(instance == null){
            instance = new Index();
        }
        return instance;
    }

    public void clear(){
        index = new ThreadSafeHashMap<>();
    }


    public Set<String> find(String word) {
        return index.get(word);
    }

    public void addWorld(String word, String file) {
        if (index.containsKey(word)) {
            index.get(word).add(file);
        } else {
            Set<String> files = new HashSet<>();
            files.add(file);
            index.put(word, files);
        }
    }

    public boolean isIndexed(){
        return !index.isEmpty();
    }

    public long index(int numThreads, List<File> files) throws InterruptedException {
        IndexerThread[] threadForIndexers = new IndexerThread[numThreads];

        Arrays.setAll(threadForIndexers, i -> new IndexerThread(files, (files.size() / numThreads) * i, (files.size() / numThreads) * (i + 1)));

        long start = System.currentTimeMillis();
        IntStream.range(0, numThreads).forEach(i1 -> threadForIndexers[i1].start());
        for (int i = 0; i < numThreads; i++) {
            threadForIndexers[i].join();
        }
        return System.currentTimeMillis() - start;
    }
}
