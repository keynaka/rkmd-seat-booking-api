package com.rkmd.toki_no_nagare.dto.payment;

import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Schema(description = "Datos del pago")
public class PaymentDto {

  @Schema(
      name = "paymentStatus",
      description = "detail: Estado del pago")
  private PaymentStatus paymentStatus;

  @Schema(
      name = "paymentMethod",
      description = "detail: Forma de pago.")
  private PaymentMethod paymentMethod;

  @Schema(
      name = "amount",
      description = "detail: Monto total a pagar",
      example = "30000")
  private BigDecimal amount;

  @Setter
  @Schema(
      name = "receiptNumber",
      description = "detail: Número de comprobante",
      example = "30000",
      nullable = true)
  private String receiptNumber;

  @Schema(
      name = "dateCreated",
      description = "detail: Fecha de creación de pago",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]")
  private ZonedDateTime dateCreated;

  @Schema(
      name = "lastUpdated",
      description = "detail: Fecha de última modificación",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]",
      nullable = true)
  @Setter
  private ZonedDateTime lastUpdated;

  @Schema(
      name = "expirationDate",
      description = "detail: Fecha de expiración.",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]")
  private ZonedDateTime expirationDate;

}
