package com.github.aguerra.htmlurlchecksum.services;


import com.github.aguerra.htmlurlchecksum.exceptions.NotADirectoryException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectoryScanner {
    private final Logger logger = Logger.getLogger("Directory Scan");
    private List<File> foundFiles;

    public DirectoryScanner(String path) {
        this.scan(path);
    }

    private void scan(String path) {
        var file = new File(path);
        if (!file.isDirectory()) throw new NotADirectoryException(path);
        var files = file.listFiles();
        if (files != null)
            this.foundFiles = Arrays.asList(files);
        else
            this.foundFiles = new ArrayList<>();
    }

    public List<File> getAllFiles(String ext) {
        return this.directoryFiles(this.foundFiles, ext);
    }

    private List<File> directoryFiles(List<File> files, String ext) {
        List<File> outPutFiles = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("." + ext)) {
                if (!file.canRead()) {
                    logger.warning(String.format("'%s' don't have read permission", file.getAbsolutePath()));
                    continue;
                }
                logger.log(
                        Level.INFO,
                        String.format(
                                "Found file '%s' in path '%s'",
                                file.getName(),
                                file.getAbsolutePath()
                        )
                );
                outPutFiles.add(file);
            } else if (file.isDirectory()) {
                var subDirectoryFiles = file.listFiles();
                if (subDirectoryFiles != null)
                    outPutFiles.addAll(
                            this.directoryFiles(
                                    Arrays.asList(subDirectoryFiles),
                                    ext
                            )
                    );
            }
        }

        return outPutFiles;
    }
}
