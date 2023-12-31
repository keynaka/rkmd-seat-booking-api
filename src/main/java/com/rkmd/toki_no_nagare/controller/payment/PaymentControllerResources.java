package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
  ChangePaymentResponseDto changePaymentStatus(@RequestBody ChangePaymentRequestDto request,
                                               @RequestHeader("x-auth-username") String userName,
                                               @RequestHeader("x-auth-password") String password);


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
          summary = "Modifica el estado del pago a canceled para vip",
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
  ChangePaymentResponseDto cancelVipPayment(@RequestBody ChangePaymentRequestDto request,
                                               @RequestHeader("x-auth-username") String userName,
                                               @RequestHeader("x-auth-password") String password);

}
