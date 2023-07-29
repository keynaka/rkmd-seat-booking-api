package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(String code, String description) {
        super(code, description, HttpStatus.BAD_REQUEST);
    }
}
