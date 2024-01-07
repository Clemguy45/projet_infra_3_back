package com.example.projet_infra_3_backend.exception;

public class UsernameExistException extends Exception {
    public UsernameExistException(String message) {
        super(message);
    }
}
