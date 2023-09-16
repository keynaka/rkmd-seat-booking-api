package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados de la butaca")
public enum SeatStatus {
    @JsonProperty("vacant")
    VACANT,
    @JsonProperty("reserved")
    RESERVED,
    @JsonProperty("occupied")
    OCCUPIED
}
