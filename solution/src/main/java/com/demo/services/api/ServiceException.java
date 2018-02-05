package com.demo.services.api;

public class ServiceException extends RuntimeException {

    private String message;

    public ServiceException(final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
