package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Reservas")
public interface BookingControllerResources {

  @Operation(hidden = true)
  public ResponseEntity<Booking> getBooking(@PathVariable("booking_id") Long id) throws Exception;

  @Operation(hidden = true)
  public ResponseEntity<Booking> save(@RequestBody @Valid Map<String, Object> json);

  @Operation(
      summary = "Obtiene una reserva según el código y el dni.",
      description = "Endpoint para la web de RKMD",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Ok",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "404",
              description = "Not Found",
              content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  BookingResponseDto getBookingByCodeAndDni(@PathVariable("bookingCode") String bookingCode, @PathVariable("dni") Long dni);


  @Operation(
      summary = "Crea una reserva",
      description = "Endpoint para la web de RKMD",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request",
              content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  CreateBookingResponseDto createBooking(@RequestBody CreateBookingRequestDto request);

}
