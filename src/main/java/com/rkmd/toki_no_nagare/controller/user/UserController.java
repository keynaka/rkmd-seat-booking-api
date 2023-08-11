package com.rkmd.toki_no_nagare.controller.user;

import com.rkmd.toki_no_nagare.dto.user.CreateUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.GetUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.UserRequestDto;
import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.service.UserService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("")
public class UserController implements UserControllerResources {
    @Autowired
    private UserService userService;

    @GetMapping("/v1/user/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable("username") String username) {
        Optional<User> user = userService.get(username);
        if (!user.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(user.get());
    }

    @PostMapping("/v1/user")
    public ResponseEntity<User> createUser(@RequestBody @Valid Map<String, Object> json) {
        ValidationUtils.checkParam(json.containsKey("username"), "username_missing", "Username is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("password"), "password_missing", "Password is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("role"), "role_missing", "Role is missing and is mandatory");

        User newUser = userService.save(json);

        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping(value = "/v2/users", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateUserResponseDto createUserV2(UserRequestDto request) {
        return userService.createUser(request.getUserName(), request.getPassword(), request.getRole());
    }

    @GetMapping(value = "/v2/users/{username}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public GetUserResponseDto getUserByNameV2(String username) {
        return userService.getUserByNameV2(username);
    }

}