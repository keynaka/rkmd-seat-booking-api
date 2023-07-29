package com.rkmd.toki_no_nagare.exception;

import org.springframework.http.HttpStatus;

public class ApiError {
    private String code;
    private String description;
    private HttpStatus status;

    public ApiError(String code, String description, HttpStatus status) {
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
