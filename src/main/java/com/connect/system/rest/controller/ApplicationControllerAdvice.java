package com.connect.system.rest.controller;

import com.connect.system.exception.RegraNegocioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex) {
        String message = ex.getMessage();
        HttpStatus httpStatus = ex.getHttpStatus();
        return new ResponseEntity<>(message, new HttpHeaders(), httpStatus);
    }

}
