package com.rkmd.toki_no_nagare.entities.booking;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rkmd.toki_no_nagare.entities.contact.Contact;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@ToString
@Entity
@Table(
    name="booking",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "hashedBookingCode", name = "UK_HashedBookingCode"),
        @UniqueConstraint(columnNames = {"hashedBookingCode", "client_id"}, name = "UK_HashedBookingCode_ClientId")
})
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

    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @Column(name = "expiration_date", nullable = false)
    private ZonedDateTime expirationDate;
    
    private String hashedBookingCode;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "paymentId")
    private Payment payment;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Seat> seats;

    public Booking(Contact contact, Payment payment, String hashedBookingCode){
        this.client = contact;
        this.payment = payment;
        this.status = BookingStatus.PENDING;
        this.dateCreated = payment.getDateCreated();
        this.expirationDate = payment.getExpirationDate();
        this.hashedBookingCode = hashedBookingCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contact getClient() {
        return client;
    }

    public void setClient(Contact client) {
        this.client = client;
    }

    public Contact getSeller() {
        return seller;
    }

    public void setSeller(Contact seller) {
        this.seller = seller;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getHashedBookingCode() {
        return hashedBookingCode;
    }

    public void setHashedBookingCode(String hashedBookingCode) {
        this.hashedBookingCode = hashedBookingCode;
    }
}