package com.rkmd.toki_no_nagare.entities.seat;

import java.io.Serializable;
import java.util.Objects;

public class SeatId implements Serializable {
    private Long row;
    private Long column;
    private SeatSector sector;

    public SeatId(Long row, Long column, SeatSector sector) {
        this.row = row;
        this.column = column;
        this.sector = sector;
    }

    public SeatId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId seatId = (SeatId) o;
        return Objects.equals(row, seatId.row) &&
                Objects.equals(column, seatId.column) &&
                Objects.equals(sector, seatId.sector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, sector);
    }
}

