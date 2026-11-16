package com.example.gilded_rose_backend.exception;

public class InvalidInventoryException extends RuntimeException {

    public InvalidInventoryException(String message) {
        super(message);
    }
}
