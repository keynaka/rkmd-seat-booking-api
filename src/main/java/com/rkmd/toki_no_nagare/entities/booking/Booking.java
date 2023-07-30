package com.rkmd.toki_no_nagare.entities.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

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

    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated", nullable = false)
    private ZonedDateTime lastUpdated;

    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;
}