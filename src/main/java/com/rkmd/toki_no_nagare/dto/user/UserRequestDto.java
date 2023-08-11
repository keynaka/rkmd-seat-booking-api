package com.rkmd.toki_no_nagare.dto.user;

import com.rkmd.toki_no_nagare.entities.user.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserRequestDto {

  @Schema(
      name = "userName",
      description = "detail: Nombre de usuario",
      example = "nico_naka",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String userName;

  @Schema(
      name = "passwordHash",
      description = "detail: Contraseña del usuario",
      example = "keynaka1234",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;

  @Schema(
      name = "role",
      description = "detail: Rol del usuario",
      example = "admin",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private RoleType role;

}