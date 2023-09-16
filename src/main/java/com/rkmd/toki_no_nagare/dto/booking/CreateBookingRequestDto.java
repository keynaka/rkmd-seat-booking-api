package com.rkmd.toki_no_nagare.dto.booking;

import com.rkmd.toki_no_nagare.dto.Contact.ContactRequestDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatRequestDto;
import com.rkmd.toki_no_nagare.entities.payment.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "Solicitud de reserva")
public class CreateBookingRequestDto {

  @Valid
  @NotNull(message = "El campo 'contact' no puede ser nulo")
  public ContactRequestDto contact;

  @Valid
  @NotNull(message = "El campo 'paymentMethod' no puede ser nulo")
  public PaymentMethod paymentMethod;

  @Valid
  @NotNull(message = "El listado de 'butacas' no puede ser nulo")
  public List<SeatRequestDto> seats;

}
