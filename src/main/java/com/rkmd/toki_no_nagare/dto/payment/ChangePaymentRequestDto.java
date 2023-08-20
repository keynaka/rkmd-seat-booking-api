package com.rkmd.toki_no_nagare.dto.payment;

import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "Datos del pago a modificar")
public class ChangePaymentRequestDto {

  @Schema(
      name = "bookingCode",
      description = "detail: Código de reserva",
      example = "e61g5er234#$524",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'bookingCode' no puede ser nulo")
  private String bookingCode;

  @Schema(
      name = "contactDni",
      description = "detail: Número de DNI con el que se registró la reserva",
      example = "11111111",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'contactDni' no puede ser nulo")
  private Long contactDni;

  @Schema(
      name = "paymentStatus",
      description = "detail: Estado del pago a modificar",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "El campo 'paymentStatus' no puede ser nulo")
  private PaymentStatus paymentStatus;

}
