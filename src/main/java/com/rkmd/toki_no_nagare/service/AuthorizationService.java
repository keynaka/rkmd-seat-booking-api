package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

  @Autowired
  private UserRepository userRepository;

  public String generatePasswordHash(String userPassword){
    return BCrypt.hashpw(userPassword, BCrypt.gensalt());
  }

  public boolean validatePassword(String userName, String userPassword){
    Optional<User> user = userRepository.findById(userName);

    if(user.isEmpty())
      throw new NotFoundException("username_not_exist", "This username not exists");

    return BCrypt.checkpw(userPassword, user.get().getPasswordHash());
  }

}
