package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserName(String username);
}
