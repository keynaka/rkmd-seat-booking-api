package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ApiException {
    public UnAuthorizedException(String code, String description) {
        super(code, description, HttpStatus.UNAUTHORIZED);
    }
}