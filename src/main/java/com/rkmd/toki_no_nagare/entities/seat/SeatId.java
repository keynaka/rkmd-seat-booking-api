package com.rkmd.toki_no_nagare.entities.seat;

import java.io.Serializable;

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
}
