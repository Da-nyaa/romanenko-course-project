package org.kpi;

import org.kpi.index.Index;
import org.kpi.utils.FileUtils;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File mainDir = new File("files");
        List<File> files = FileUtils.getListOfFiles(mainDir);
        System.out.println(files.size());
        System.out.println(files);

        try {
            Index.getInstance().index(10, files);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Index.getInstance().isIndexed());

        System.out.println(Index.getInstance().find("hello"));
    }
}
