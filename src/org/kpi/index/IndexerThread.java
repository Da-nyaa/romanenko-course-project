package org.kpi.index;

import org.kpi.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IndexerThread extends Thread {
    List<File> files;
    int start;
    int end;

    public IndexerThread(List<File> files, int start, int end) {
        this.files = files;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            try {
                FileUtils.fillIndex(files.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}