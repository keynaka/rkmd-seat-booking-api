package com.rkmd.toki_no_nagare.controller.user;

import com.rkmd.toki_no_nagare.dto.user.CreateUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.GetUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.UserRequestDto;
import com.rkmd.toki_no_nagare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class UserController implements UserControllerResources {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/v2/users", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateUserResponseDto createUser(UserRequestDto request) {
        return userService.createUser(request.getUserName(), request.getPassword());
    }

    @GetMapping(value = "/v2/users/{username}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public GetUserResponseDto getUserByName(String username) {
        return userService.getUserByNameV2(username);
    }

}