package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SeatStatus {
    @JsonProperty("vacant")
    VACANT,
    @JsonProperty("reserved")
    RESERVED,
    @JsonProperty("occupied")
    OCCUPIED
}
