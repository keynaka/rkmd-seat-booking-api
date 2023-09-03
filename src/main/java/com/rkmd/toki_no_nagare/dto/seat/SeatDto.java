package com.rkmd.toki_no_nagare.dto.seat;

import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Schema(description = "Datos de la butaca")
@AllArgsConstructor
public class SeatDto {

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
      name = "price",
      description = "detail: Precio de la butaca.",
      example = "3000")
  private BigDecimal price;

  @Schema(
      name = "sector",
      description = "detail: Nombre del sector a la que pertenece la butaca.")
  private SeatSector sector;

  @Schema(
      name = "status",
      description = "detail: Estado de la butaca.")
  private SeatStatus status;

}
