package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class RequestTimeoutException extends ApiException {
    public RequestTimeoutException(String code, String description) {
        super(code, description, HttpStatus.REQUEST_TIMEOUT);
    }
}