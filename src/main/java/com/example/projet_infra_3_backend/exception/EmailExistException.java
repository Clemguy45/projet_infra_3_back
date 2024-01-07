package com.example.projet_infra_3_backend.exception;

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
