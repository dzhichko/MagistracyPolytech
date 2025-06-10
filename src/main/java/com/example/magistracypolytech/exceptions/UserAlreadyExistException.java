package com.example.magistracypolytech.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("User already exist");
    }
}
