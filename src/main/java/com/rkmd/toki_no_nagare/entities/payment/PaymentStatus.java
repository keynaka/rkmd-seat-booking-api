package com.rkmd.toki_no_nagare.entities.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Estados del pago")
public enum PaymentStatus {

  @JsonProperty("pending")
  PENDING,
  @JsonProperty("paid")
  PAID,
  @JsonProperty("expired")
  EXPIRED,
  @JsonProperty("canceled")
  CANCELED

}
