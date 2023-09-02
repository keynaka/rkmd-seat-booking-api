package com.rkmd.toki_no_nagare.dto.seat;

import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Schema(description = "Datos de la butaca")
public class PrereserveSeatDto {

    @Schema(
            name = "row",
            description = "detail: Número de fila (eje x)",
            example = "8")
    private Long row;

    @Schema(
            name = "column",
            description = "detail: Número de columna (eje y)",
            example = "10")
    private Long column;

    @Schema(
            name = "sector",
            description = "detail: Nombre del sector a la que pertenece la butaca.")
    private SeatSector sector;
}