package com.okta.example;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

public class FileContentsChecker {

    private final Set<String> preparedFilePaths = new LinkedHashSet<String>();

    private void preparePaths(String [] filenames) throws IOException {
        for (String s: filenames) {
            File presFile = new File(s);
            if (presFile.isDirectory()){
                Files.walk(Paths.get(presFile.getAbsolutePath()))
                        .filter(Files::isRegularFile)
                        .forEach(file -> preparedFilePaths.add(file.toString()));
            } else {
                preparedFilePaths.add(presFile.getAbsolutePath());
            }
        }
    }

    private boolean fileContainsWord(String fileName, String word) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName))).toLowerCase().contains(word.toLowerCase());
    }

    private boolean findCrashBuildWords(Set<String> fileNames, String [] words) throws IOException {
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
