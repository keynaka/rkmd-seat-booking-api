package com.rkmd.toki_no_nagare.entities.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BookingPaymentMethod {
    @JsonProperty("mercado_pago")
    MERCADO_PAGO,
    @JsonProperty("cash")
    CASH,
    @JsonProperty("debit")
    DEBIT,
    @JsonProperty("credit")
    CREDIT
}
