package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.exception.ApiError;
import com.rkmd.toki_no_nagare.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException e) {
        ApiError apiError = new ApiError(e.getCode(), e.getDescription(), e.getStatus());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
