package com.rkmd.toki_no_nagare.entities.contact;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Entity
@ToString
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

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.ALL) //TODO: Change to LAZY if possible
    private List<Booking> bookings;

    public Contact(Long dni){
        this.dni = dni;
    }

    public Contact(Long dni, String name, String lastName, String email, String phone){
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}
