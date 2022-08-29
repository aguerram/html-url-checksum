package com.github.aguerra.htmlurlchecksum.services;

import java.io.File;
import java.util.List;

public class HtmlChecksumCalculator {
    private final List<File> files;

    public HtmlChecksumCalculator(String path, List<File> files) {
        this.files = files;
    }
}
