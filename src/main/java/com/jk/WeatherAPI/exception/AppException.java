package com.jk.WeatherAPI.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public AppException(final HttpStatus httpStatus, final String errorMessage) {
        super(errorMessage);
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public AppException(final int statusCode, final String reason) {
        this(HttpStatus.valueOf(statusCode), reason);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
