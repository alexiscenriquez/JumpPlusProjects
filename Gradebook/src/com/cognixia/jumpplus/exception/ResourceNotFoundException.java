package com.cognixia.jumpplus.exception;

public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resource,int id) {
        // calls the Exception(String msg) constructor
        super(resource+" with id "+ id+ " was not found");
    }
}
