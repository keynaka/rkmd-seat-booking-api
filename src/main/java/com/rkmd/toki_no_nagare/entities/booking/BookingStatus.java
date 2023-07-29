package com.rkmd.toki_no_nagare.entities.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BookingStatus {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("paid")
    PAID,
    @JsonProperty("expired")
    EXPIRED
}
