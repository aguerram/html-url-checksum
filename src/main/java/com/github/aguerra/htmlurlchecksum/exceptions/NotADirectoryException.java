package com.github.aguerra.htmlurlchecksum.exceptions;

public class NotADirectoryException extends RuntimeException {
    public NotADirectoryException(String path) {
        super(
                String.format("'%s' is not a directory", path)
        );
    }
}
