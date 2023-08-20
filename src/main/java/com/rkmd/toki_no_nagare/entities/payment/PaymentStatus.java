package com.rkmd.toki_no_nagare.entities.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

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
