package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.service.AuthorizationService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping(value = "/v1/login", produces = "application/json")
    public ResponseEntity<?> loginValidation(@RequestHeader("x-auth-username") String userName,
                                   @RequestHeader("x-auth-password") String password) {
        if (!authorizationService.validatePassword(userName, password))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/v1/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        ValidationUtils.checkParam(body.containsKey("username"), "invalid_body", "username is missing");
        ValidationUtils.checkParam(body.containsKey("password"), "invalid_body", "password is missing");

        if (!authorizationService.validatePassword(body.get("username"), body.get("password")))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return ResponseEntity.ok().body(body);
    }
}
