package com.rkmd.toki_no_nagare.entities.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
