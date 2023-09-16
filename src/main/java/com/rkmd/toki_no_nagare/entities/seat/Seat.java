package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.utils.Tools;
import jakarta.persistence.*;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@IdClass(SeatId.class)
@ToString
@Table(name="seat")
public class Seat {
    @Id
    @Column(name = "row", nullable = false)
    private Long row;

    @Id
    @Column(name = "column", nullable = false)
    private Long column;

    private BigDecimal price;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "sector", nullable = false)
    private SeatSector sector;

    @Column(name = "auxiliar_column")
    private Integer auxiliarColumn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @Version
    @Column(name = "version")
    private Long version;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public Long getColumn() {
        return column;
    }

    public void setColumn(Long column) {
        this.column = column;
    }

    public SeatSector getSector() {
        return sector;
    }

    public void setSector(SeatSector sector) {
        this.sector = sector;
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

    public Integer getAuxiliarColumn() {
        return auxiliarColumn;
    }

    public void setAuxiliarColumn(Integer auxiliarColumn) {
        this.auxiliarColumn = auxiliarColumn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @PrePersist
    protected void onCreate() {
        dateCreated = Tools.getCurrentDate();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = Tools.getCurrentDate();
    }
}
