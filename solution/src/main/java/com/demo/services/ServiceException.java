package com.demo.services;

import com.demo.controller.dto.FieldErrorDTO;
import java.util.List;

public class ServiceException extends RuntimeException {

    private List<FieldErrorDTO> fieldErrors;
    private String message;


    public ServiceException(final List<FieldErrorDTO> fieldErrors, final String message) {
        super(message);
        this.fieldErrors = fieldErrors;
        this.message = message;
    }
}
