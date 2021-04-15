package com.okta.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileContentsChecker {

    private final List<String> preparedFilePaths = new ArrayList<String>();

    private void preparePaths(String [] filenames){
        for (String fileName : filenames){
            File file = new File(fileName);
            if (file.isDirectory()){
                List<String> paths = Arrays.stream(Objects.requireNonNull(file.listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());
                preparedFilePaths.addAll(paths);
            } else {
                preparedFilePaths.add(file.getAbsolutePath());
            }
        }
    }

    private boolean fileContainsWord(String fileName, String word) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName))).toLowerCase().contains(word.toLowerCase());
    }

    private boolean findCrashBuildWords(List<String> fileNames, String [] words) throws IOException {
        for (String fileName : fileNames) {
            for (String word : words) {
                if (fileContainsWord(fileName, word)) return true;
            }
        }
        return false;
    }

    public boolean startCheck(String [] fileNames, String [] words) throws IOException, NoSuchFileException {
        preparePaths(fileNames);
        return findCrashBuildWords(preparedFilePaths, words);
    }

}
