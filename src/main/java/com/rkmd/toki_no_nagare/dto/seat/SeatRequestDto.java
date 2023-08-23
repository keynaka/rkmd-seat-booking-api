package com.rkmd.toki_no_nagare.dto.seat;

import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de la butaca")
public class SeatRequestDto {

  @Schema(
      name = "row",
      description = "detail: Número de fila (eje x)",
      example = "8",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long row;

  @Schema(
      name = "column",
      description = "detail: Número de columna (eje y)",
      example = "10",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long column;

  @Schema(
      name = "sector",
      description = "detail: Nombre del sector a la que pertenece la butaca.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private SeatSector sector;

}
