package com.rkmd.toki_no_nagare.entities.seat;

import java.io.Serializable;

public class SeatId implements Serializable {
    private String row;
    private String column;

    public SeatId(String row, String column) {
        this.row = row;
        this.column = column;
    }

    public SeatId() {}
}
