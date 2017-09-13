package com.github.cc.gate.gateway.exception;

public class AuthenticationServerErrorException extends RuntimeException {
    public AuthenticationServerErrorException(String message) {
        super(message);
    }
}
