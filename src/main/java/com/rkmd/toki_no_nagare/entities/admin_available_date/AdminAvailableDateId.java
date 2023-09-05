package com.rkmd.toki_no_nagare.entities.admin_available_date;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class AdminAvailableDateId implements Serializable {
    private ZonedDateTime initDate;
    private String place;

    public AdminAvailableDateId(ZonedDateTime initDate, String place) {
        this.initDate = initDate;
        this.place = place;
    }

    public AdminAvailableDateId() {}
}