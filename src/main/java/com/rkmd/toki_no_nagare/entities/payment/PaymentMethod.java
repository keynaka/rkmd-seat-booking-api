package com.rkmd.toki_no_nagare.entities.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formas de pago")
public enum PaymentMethod {

  @JsonProperty("mercado_pago")
  MERCADO_PAGO,
  @JsonProperty("cash")
  CASH

}
