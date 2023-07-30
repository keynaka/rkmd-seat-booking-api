package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SeatSector {
    @JsonProperty("pullman")
    PULLMAN,
    @JsonProperty("platea")
    PLATEA
}
