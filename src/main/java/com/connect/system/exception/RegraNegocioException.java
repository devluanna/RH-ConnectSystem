package com.connect.system.exception;

import org.springframework.http.HttpStatus;


public class RegraNegocioException extends RuntimeException {

    private final HttpStatus httpStatus;
    public RegraNegocioException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
