package org.kpi.utils;

import org.kpi.index.Index;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public static List<File> fileList = new ArrayList<>();

    public static void fillIndex(File file) throws IOException {
        String text = getTextFromFile(file);
        String[] words = text != null ? text.split("\\W") : new String[0];
        List<String> cleanList = Arrays.stream(words).filter(str -> !str.isEmpty()).toList();
        for (String word : cleanList) {
            Index.getInstance().addWorld(word.toLowerCase(), file.toString());
        }
    }

    public static List<File> getListOfFiles(File directory) {

        if (directory.isDirectory()) {
            File[] temp = directory.listFiles();
            Arrays.stream(temp).forEach(FileUtils::getListOfFiles);
        } else {
            fileList.add(directory);
        }
        return fileList;
    }

    private static String getTextFromFile(File file) {
        try {
            byte[] encodedBytes = Files.readAllBytes(file.toPath());
            return new String(encodedBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
