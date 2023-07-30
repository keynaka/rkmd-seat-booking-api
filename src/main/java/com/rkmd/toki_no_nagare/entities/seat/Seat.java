package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import jakarta.persistence.*;

@Entity
@IdClass(SeatId.class)
@Table(name="seat")
public class Seat {
    @Id
    @Column(name = "row", nullable = false)
    private String row;

    @Id
    @Column(name = "column", nullable = false)
    private String column;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
