package com.example.testing.exceptions;

public class PasswordNotMatchException extends Exception{
    public PasswordNotMatchException(String message) {
        super(message);
    }
}
