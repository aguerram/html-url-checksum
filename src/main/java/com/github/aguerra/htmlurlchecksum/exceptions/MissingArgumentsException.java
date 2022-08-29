package com.github.aguerra.htmlurlchecksum.exceptions;

public class MissingArgumentsException extends RuntimeException{
    public MissingArgumentsException(String accessor){
        super(
                String.format("Command %s requires additional arguments, use 'help' for more details.",accessor)
        );
    }
}
