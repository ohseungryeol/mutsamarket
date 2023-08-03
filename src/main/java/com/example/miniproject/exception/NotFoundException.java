package com.example.miniproject.exception;

public class NotFoundException extends Status400Exception{
    public NotFoundException(String message) {
        super(message);
    }
}
