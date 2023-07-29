package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.user.RoleType;
import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.ContactRepository;
import com.rkmd.toki_no_nagare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    public Optional<User> get(String username) {
        return userRepository.findById(username);
    }

    public User save(Map<String, Object> json) {
        if (userRepository.findById((String) json.get("username")).isPresent())
            throw new BadRequestException("username_already_exists", "This username already exists");
        User newUser = new User();

        newUser.setUserName((String) json.get("username"));
        newUser.setPasswordHash((String) json.get("password"));
        newUser.setRole(RoleType.valueOf(((String) json.get("role")).toUpperCase()));

        try {
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }
}