package com.xssattack.exception;

public class XSSServletException extends RuntimeException {

    public XSSServletException() {
    }

    public XSSServletException(String message) {
        super(message);
    }
}
