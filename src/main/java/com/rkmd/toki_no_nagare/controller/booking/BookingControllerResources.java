package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Reservas")
public interface BookingControllerResources {

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


  @Operation(
      summary = "Obtiene una reserva según el código de reserva.",
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
  ResponseEntity<BookingStatus> getBookingStatus(@PathVariable("code_id") String codeId);

  @Operation(
          summary = "Crea una reserva VIP",
          description = "Crea una reserva VIP",
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
  ChangePaymentResponseDto createBookingVip(@RequestBody CreateBookingRequestDto request,
                                            @RequestHeader("x-auth-username") String userName,
                                            @RequestHeader("x-auth-password") String password);

}
