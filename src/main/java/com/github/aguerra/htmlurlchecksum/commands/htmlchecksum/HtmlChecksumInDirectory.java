package com.github.aguerra.htmlurlchecksum.commands.htmlchecksum;

import com.github.aguerra.htmlurlchecksum.commands.AbstractCommand;
import com.github.aguerra.htmlurlchecksum.exceptions.MissingArgumentsException;
import com.github.aguerra.htmlurlchecksum.services.DirectoryScanner;
import com.github.aguerra.htmlurlchecksum.services.HtmlChecksumCalculator;

import java.util.Arrays;
import java.util.List;

public class HtmlChecksumInDirectory implements AbstractCommand {
    @Override
    public String[] accessors() {
        return new String[]{
                "-d",
                "--directory"
        };
    }

    @Override
    public String description() {
        return "Command to list html files in a directory and add checksum to urls found on each html file";
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public void run(List<String> args) {
        if (args.size() == 0) throw new MissingArgumentsException(Arrays.toString(this.accessors()));
        String path = args.get(0);
        DirectoryScanner directoryScanner = new DirectoryScanner(path);
        var htmlFiles = directoryScanner.getAllFiles("html");
        HtmlChecksumCalculator htmlChecksumCalculator = new HtmlChecksumCalculator(path, htmlFiles);
    }
}
