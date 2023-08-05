package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SeatStatus {
    @JsonProperty("vacant")
    VACANT,
    @JsonProperty("pre_reserved")
    PRE_RESERVED, // Status when the seat was selected by te user to reserve it
    @JsonProperty("reserved")
    RESERVED,
    @JsonProperty("occupied")
    OCCUPIED
}
