package com.example.GftApplication.exceptions.customs;

public class UniqueConstraintViolationException extends Exception {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
