package com.example.GftApplication.exceptions;

public class UniqueConstraintViolationException extends Exception {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
