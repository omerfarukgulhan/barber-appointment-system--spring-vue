package com.ofg.barber.controller;

import java.util.stream.Collectors;

import com.ofg.barber.core.util.message.Messages;
import com.ofg.barber.core.util.results.ApiErrorResponse;
import com.ofg.barber.exception.authentication.*;
import com.ofg.barber.exception.email.ActivationNotificationException;
import com.ofg.barber.exception.email.EmailServiceException;
import com.ofg.barber.exception.file.FileServiceException;
import com.ofg.barber.exception.general.NotFoundException;
import com.ofg.barber.exception.general.NotUniqueEmailException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler({
            ActivationNotificationException.class,
            AuthenticationException.class,
            EmailServiceException.class,
            FileServiceException.class,
            InvalidPasswordException.class,
            InvalidTokenException.class,
            MethodArgumentNotValidException.class,
            MissingAuthorizationHeaderException.class,
            NotFoundException.class,
            NotUniqueEmailException.class,
            UserInactiveException.class
    })
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMessage(exception.getMessage());

        int status = 500;
        if (exception instanceof ActivationNotificationException) {
            status = 502;
        } else if (exception instanceof AuthenticationException) {
            status = 401;
        } else if (exception instanceof EmailServiceException) {
            status = 500;
        } else if (exception instanceof FileServiceException) {
            status = 500;
        } else if (exception instanceof InvalidPasswordException) {
            status = 400;
        } else if (exception instanceof InvalidTokenException) {
            status = 400;
        } else if (exception instanceof MethodArgumentNotValidException) {
            String message = Messages.getMessageForLocale("app.msg.error.validation", LocaleContextHolder.getLocale());
            apiErrorResponse.setMessage(message);
            status = 400;
            var validationErrors = ((MethodArgumentNotValidException) exception)
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacing) -> existing));
            apiErrorResponse.setValidationErrors(validationErrors);
        } else if (exception instanceof MissingAuthorizationHeaderException) {
            status = 401;
        } else if (exception instanceof NotFoundException) {
            status = 404;
        } else if (exception instanceof NotUniqueEmailException) {
            status = 400;
            apiErrorResponse.setValidationErrors(((NotUniqueEmailException) exception).getValidationErrors());
        } else if (exception instanceof UserInactiveException) {
            status = 403;
        }

        apiErrorResponse.setStatus(status);
        return ResponseEntity.status(status).body(apiErrorResponse);
    }
}