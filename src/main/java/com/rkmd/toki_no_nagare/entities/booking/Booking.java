package com.rkmd.toki_no_nagare.entities.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import jakarta.persistence.*;

@Entity
@Table(name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Contact client;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Contact seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private BookingPaymentMethod paymentMethod;
}