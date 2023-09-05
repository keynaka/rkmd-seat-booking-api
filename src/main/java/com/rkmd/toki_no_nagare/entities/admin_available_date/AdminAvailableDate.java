package com.rkmd.toki_no_nagare.entities.admin_available_date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Entity
@Getter
@Setter
@IdClass(AdminAvailableDateId.class)
@Table(name="admin_available_date")
public class AdminAvailableDate {
    @Id
    @Column(name = "init_date")
    private ZonedDateTime initDate;

    @Id
    @Column(name = "place")
    private String place;

    @Column(name = "end_date")
    private ZonedDateTime endDate;
}
