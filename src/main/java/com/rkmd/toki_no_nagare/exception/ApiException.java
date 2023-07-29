package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    protected String code;
    protected String description;
    protected HttpStatus status;
    public ApiException(String code, String description, HttpStatus status) {
        super(description);
        this.code = code;
        this.description = description;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
