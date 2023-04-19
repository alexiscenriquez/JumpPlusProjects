package com.cognixia.jumpplus.exception;

public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(int msg) {
        // calls the Exception(String msg) constructor
        super("Teacher with id "+ msg+ " was not found");
    }
}
