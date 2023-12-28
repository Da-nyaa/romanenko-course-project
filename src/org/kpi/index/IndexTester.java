package org.kpi.index;

import org.kpi.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IndexTester {
    public static void main(String[] args) {
        int[] threadsNumber = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384};

        List<File> fileList = FileUtils.getListOfFiles(new File("files"));
//        for(int n : threadsNumber){
//            Index.getInstance().clear();
//            try {
//                long time = Index.getInstance().index(n, fileList);
//                System.out.println("Кількість потоків: " + n + ". Bитрачено часу: " + time + " мс.");
//                writeToCSV(n, time);
//            } catch (InterruptedException ignored) {
//            }
//        }

        for(int i = 1; i <= 1001; i += 10){
            Index.getInstance().clear();
            try {
                long time = Index.getInstance().index(i, fileList);
                System.out.println("Кількість потоків: " + i + ". Bитрачено часу: " + time + " мс.");
                writeToCSV(i, time);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void writeToCSV(int threadCount, long elapsedTime) {
        String csvFile = "data2.csv";

        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.append(String.valueOf(threadCount));
            writer.append(",");
            writer.append(String.valueOf(elapsedTime));
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
