package com.github.aguerra.htmlurlchecksum.commands;

import java.util.List;

public interface AbstractCommand {
    String[] accessors();

    default String[] additionalAccessors() {
        return new String[]{};
    }

    String description();

    String help();

    void run(List<String> args);
}
