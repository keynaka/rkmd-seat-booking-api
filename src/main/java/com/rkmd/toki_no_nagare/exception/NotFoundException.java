package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String code, String description) {
        super(code, description, HttpStatus.NOT_FOUND);
    }
}
