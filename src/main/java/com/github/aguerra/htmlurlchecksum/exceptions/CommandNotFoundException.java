package com.github.aguerra.htmlurlchecksum.exceptions;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(String accessor) {
        super(
                String.format(
                        "Command '%s' was not found"
                        ,
                        accessor
                )
        );
    }
}
