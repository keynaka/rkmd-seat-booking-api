package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.user.User;
import com.rkmd.toki_no_nagare.exception.NotFoundException;
import com.rkmd.toki_no_nagare.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {
  private UserRepository userRepository;
  private Map<String, String> adminCache;

  public AuthorizationService(UserRepository userRepository) {
    this.userRepository = userRepository;
    reloadCache();
  }

  public String generatePasswordHash(String userPassword){
    return BCrypt.hashpw(userPassword, BCrypt.gensalt());
  }

  public boolean validatePassword(String userName, String userPassword){
    if(!adminCache.containsKey(userName))
      throw new NotFoundException("username_not_exist", "This username not exists");

    return BCrypt.checkpw(userPassword, adminCache.get(userName));
  }

  public void reloadCache() {
    adminCache = userRepository.findAll()
            .stream()
            .collect(Collectors.toMap(User::getUserName, User::getPasswordHash));
  }

}
