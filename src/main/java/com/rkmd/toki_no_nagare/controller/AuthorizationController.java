package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping(value = "/v1/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestHeader("x-auth-username") String userName,
                                   @RequestHeader("x-auth-password") String password) {
        if (!authorizationService.validatePassword(userName, password))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
