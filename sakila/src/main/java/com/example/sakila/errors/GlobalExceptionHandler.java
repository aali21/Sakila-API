package com.example.sakila.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {

        // Create a ResponseEntity with the reason/message of the ResponseStatusException
        // and the corresponding HTTP status code
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String error = "JSON parse error: " + ex.getMessage();

        // Check if the cause of the HttpMessageNotReadableException is an InvalidFormatException,
        // which commonly occurs when Jackson fails to deserialize an invalid enum value from JSON.
        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            com.fasterxml.jackson.databind.exc.InvalidFormatException ife = (com.fasterxml.jackson.databind.exc.InvalidFormatException) ex.getCause();
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                error = "Invalid value for enum " + ife.getTargetType().getSimpleName() + ": " + ife.getValue()+". Can only be in either of these format [NC_17, R, G, PG_13, PG]";
            }
        }
        return ResponseEntity.badRequest().body(error);
    }
}
