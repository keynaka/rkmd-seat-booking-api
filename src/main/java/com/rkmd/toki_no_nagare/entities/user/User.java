package com.rkmd.toki_no_nagare.entities.user;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
