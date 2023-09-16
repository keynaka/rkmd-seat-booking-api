package com.rkmd.toki_no_nagare.controller.report;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.report.BookingStatisticsDto;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReportControllerResources {

  @Operation(
      summary = "Obtiene una reserva según el código y el dni.",
      description = "Endpoint para la web de RKMD - Solo ADMIN",
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
      summary = "Obtiene los datos de una reserva según el código.",
      description = "Endpoint para la web de RKMD - Solo ADMIN",
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
  ResponseEntity<BookingResponseDto> getBookingReport(@PathVariable("code_id") String codeId);


  @Operation(
      summary = "Obtiene el estado general de las reservas.",
      description = "Endpoint para la web de RKMD - Solo ADMIN",
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
  BookingStatisticsDto getGeneralStatus();
}
