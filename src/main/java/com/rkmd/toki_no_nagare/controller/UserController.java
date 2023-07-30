package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.service.UserService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable("username") String username) {
        Optional<User> user = userService.get(username);
        if (!user.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(user.get());
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody @Valid Map<String, Object> json) {
        ValidationUtils.checkParam(json.containsKey("username"), "username_missing", "Username is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("password"), "password_missing", "Password is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("role"), "role_missing", "Role is missing and is mandatory");

        User newUser = userService.save(json);

        return ResponseEntity.ok().body(newUser);
    }
}