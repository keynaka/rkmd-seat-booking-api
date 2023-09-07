package com.rkmd.toki_no_nagare.controller.user;

import com.rkmd.toki_no_nagare.dto.user.CreateUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.GetUserResponseDto;
import com.rkmd.toki_no_nagare.dto.user.UserRequestDto;
import com.rkmd.toki_no_nagare.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Usuario")
public interface UserControllerResources {

  @Operation(
      summary = "Crea un usuario interno de Matsuri Daiko.",
      description = "Endpoint de uso interno",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "Created",
              useReturnTypeSchema = true),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error",
              content = @Content(schema = @Schema(implementation = ApiError.class)))})
  CreateUserResponseDto createUser(@RequestBody UserRequestDto request);


  @Operation(
      summary = "Obtiene los datos del usuario solicitado.",
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
  GetUserResponseDto getUserByName(@PathVariable("username") String username);

}