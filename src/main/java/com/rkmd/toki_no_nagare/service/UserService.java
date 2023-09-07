package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.dto.user.CreateUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.GetUserResponseDto;
import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.ContactRepository;
import com.rkmd.toki_no_nagare.repositories.UserRepository;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthorizationService authorizationService;

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

        ValidationUtils.checkParam(!((String) json.get("password")).isEmpty(), "invalid_password", "Invalid Password");

        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw((String) json.get("password"), salt);

        // TODO: To check if a password matches a hashed value later (Move later to AuthorizationService)
        boolean passwordMatches = BCrypt.checkpw((String) json.get("password"), hashedPassword);
        System.out.println("Password matches: " + passwordMatches);

        newUser.setUserName((String) json.get("username"));
        newUser.setPasswordHash(hashedPassword);

        try {
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    public CreateUserResponseDto createUser(String userName, String password){
        isUserRegistered(userName);
        isValidPassword(password);

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPasswordHash(authorizationService.generatePasswordHash(password));
        userRepository.saveAndFlush(newUser);

        return new CreateUserResponseDto(newUser.getUserName());
    }

    public GetUserResponseDto getUserByNameV2(String username){
        Optional<User> optUser = userRepository.findById(username);
        if(optUser.isEmpty()){
            throw new NotFoundException("user_not_exist", "The requested user does not exist");
        }
        return mapper.map(optUser.get(), GetUserResponseDto.class);
    }

    public void isUserRegistered(String username){
        if (userRepository.findById(username).isPresent())
            throw new BadRequestException("username_already_exists", "This username already exists");
    }

    public void isValidPassword(String password){
        if (password == null)
            throw new BadRequestException("invalid_password", "Invalid Password");
    }

}