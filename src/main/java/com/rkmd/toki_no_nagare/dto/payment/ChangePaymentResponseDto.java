package com.rkmd.toki_no_nagare.dto.payment;

import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(description = "Estado final de la reserva.")
public class ChangePaymentResponseDto {

  @Schema(
      name = "bookingCode",
      description = "detail: Código de reserva",
      example = "e61g5er234#$524")
  private String bookingCode;

  @Schema(
      name = "bookingStatus",
      description = "detail: Estado final de la reserva")
  private BookingStatus bookingStatus;

  @Schema(
      name = "message",
      description = "detail: Mensaje final de la operación",
      example = "Pago modificado exitosamente. ")
  private String message;

}
