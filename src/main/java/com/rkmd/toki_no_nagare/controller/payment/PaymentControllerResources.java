package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.BalanceResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentRequestDto;
import com.rkmd.toki_no_nagare.dto.payment.PaymentResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentResponseDto;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Operaciones de pago")
public interface PaymentControllerResources {

  @Operation(
      summary = "Modifica el estado del pago.",
      description = "Endpoint de uso interno",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Ok",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request",
              content = @Content(schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  ChangePaymentResponseDto changePaymentStatus(@RequestBody ChangePaymentRequestDto request);


  @Operation(
      summary = "Obtiene todos los pagos registrados, sin importar su estado.",
      description = "Endpoint de uso interno",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Ok",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  PaymentResponseDto getAllPayment();


  @Operation(
      summary = "Obtiene un balance general de las reservas al dia de la fecha.",
      description = """
           Endpoint de uso interno. Retorna los siguientes datos:
           - Balance de reservas concretadas, pendientes, libres y canceladas
           - Balance de montos recaudados y por recaudar
          """,
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Ok",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  BalanceResponseDto getBalance();

}
