package com.github.cc.gate.gateway.exception;

public class AuthenticationVerifyFailException extends RuntimeException {
    public AuthenticationVerifyFailException(String message) {
        super(message);
    }
}
