package com.rkmd.toki_no_nagare.entities.admin_available_date;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class AdminAvailableDateId implements Serializable {
    private ZonedDateTime initDate;
    private String place;

    public AdminAvailableDateId(ZonedDateTime initDate, String place) {
        this.initDate = initDate;
        this.place = place;
    }

    public AdminAvailableDateId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminAvailableDateId that = (AdminAvailableDateId) o;
        return Objects.equals(initDate, that.initDate) &&
                Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initDate, place);
    }
}