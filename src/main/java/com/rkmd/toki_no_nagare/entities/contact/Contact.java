package com.rkmd.toki_no_nagare.entities.contact;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Entity
@Table(name="contact")
public class Contact {

    @Id
    @Column(name = "dni", nullable = false)
    private Long dni;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @JsonManagedReference
    @OneToOne(mappedBy = "contact", cascade = CascadeType.ALL)
    private User user;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.ALL) //TODO: Change to LAZY if possible
    private List<Booking> bookings;

    public Contact(Long dni){
        this.dni = dni;
    }

    public Contact(Long dni, String name, String lastName, String email, String phone, PhoneType phoneType){
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.phoneType = phoneType;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }
}
