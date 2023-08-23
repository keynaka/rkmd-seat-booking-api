package com.rkmd.toki_no_nagare.entities.seat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Nombre de los sectores de las butacas")
public enum SeatSector {
    @JsonProperty("pullman")
    PULLMAN,
    @JsonProperty("platea")
    PLATEA,
    @JsonProperty("palcos")
    PALCOS
}
