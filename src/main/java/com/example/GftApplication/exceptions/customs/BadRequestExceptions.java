package com.example.GftApplication.exceptions.customs;

public class BadRequestExceptions extends RuntimeException {
  public BadRequestExceptions(final String errorMessage) {
    super(errorMessage);
  }
}
