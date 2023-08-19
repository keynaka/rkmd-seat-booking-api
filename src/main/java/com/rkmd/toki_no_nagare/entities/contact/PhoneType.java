package com.rkmd.toki_no_nagare.entities.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de teléfono")
public enum PhoneType {

  @JsonProperty("cellphone")
  CELLPHONE,
  @JsonProperty("phone")
  PHONE
}