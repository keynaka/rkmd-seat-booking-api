package com.rkmd.toki_no_nagare.dto.payment;

import com.rkmd.toki_no_nagare.dto.Contact.ContactDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatDto;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Estado final de la reserva.")
public class ChangePaymentResponseDto {

  @Schema(
      name = "bookingCode",
      description = "detail: Código de reserva",
      example = "e6-g_E")
  private String bookingCode;

  @Schema(
      name = "bookingStatus",
      description = "detail: Estado de la reserva")
  private BookingStatus status;

  @Schema(
      name = "dateCreated",
      description = "detail: Fecha de creación de la reserva",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]")
  private ZonedDateTime dateCreated;

  @Schema(
      name = "lastUpdated",
      description = "detail: Fecha de última modificación de la reserva",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]",
      nullable = true)
  private ZonedDateTime lastUpdated;

  @Schema(
      name = "expirationDate",
      description = "detail: Fecha de expiración de la reserva",
      example = "2023-08-17T15:30:45.123456789-03:00[America/Argentina/Buenos_Aires]")
  private ZonedDateTime expirationDate;

  private ContactDto contact;

  private PaymentDto payment;

  private List<SeatDto> seats;

}
