package com.example.miniproject.exception;

public abstract class Status400Exception extends RuntimeException {
    public Status400Exception(String message) {
        super(message);
    }
}
