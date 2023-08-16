package com.rkmd.toki_no_nagare.dto.seat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Schema(description = "Precios de butacas según el sector")
public class SeatPricesBySectorDto {

  @Schema(
      name = "pullmanSeatPrices",
      description = "detail: Precio de las butacas del sector PULLMAN.",
      example = "30000",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'pullmanSeatPrices' no puede ser nulo")
  public BigDecimal pullmanSeatPrices;

  @Schema(
      name = "plateaSeatPrices",
      description = "detail: Precio de las butacas del sector PLATEA.",
      example = "2000",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'plateaSeatPrices' no puede ser nulo")
  public BigDecimal plateaSeatPrices;

  @Schema(
      name = "palcoSeatPrices",
      description = "detail: Precio de las butacas del sector PALCO.",
      example = "1000",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'palcoSeatPrices' no puede ser nulo")
  public BigDecimal palcoSeatPrices;

}
