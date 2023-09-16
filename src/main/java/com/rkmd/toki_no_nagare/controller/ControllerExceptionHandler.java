package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.exception.ApiError;
import com.rkmd.toki_no_nagare.exception.ApiException;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private AbstractMailingService mailingService;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException e) {
        ApiError apiError = new ApiError(e.getCode(), e.getDescription(), e.getStatus());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        mailingService.notifyServiceException(e);
        ApiError apiError = new ApiError("server_internal_error", "Internal error occurred on the server", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
