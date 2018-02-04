package com.demo;

import com.demo.controller.dto.ErrorDTO;
import com.demo.controller.dto.ValidationErrorDTO;
import com.demo.services.api.ServiceException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.demo.controller")
public class RestErrorHandler {

    public static final String MESSAGE_SERVICE_UNAVAILABLE = "Service unavailable.";
    private final MessageSource messageSource;

    @Autowired
    public RestErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO procesServiceException(final ServiceException ex) {
        final ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setUuid(UUID.randomUUID().toString());
        errorDTO.setMessage(ex.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO processException(final Exception ex) {
        final ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setUuid(UUID.randomUUID().toString());
        errorDTO.setMessage(MESSAGE_SERVICE_UNAVAILABLE);
        return errorDTO;
    }

    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        final ValidationErrorDTO dto = new ValidationErrorDTO();
        dto.setMessage("validation error");
        dto.setUuid(UUID.randomUUID().toString());

        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        return localizedErrorMessage;
    }
}
