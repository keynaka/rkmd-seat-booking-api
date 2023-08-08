package com.rkmd.toki_no_nagare.dto.user;

import com.rkmd.toki_no_nagare.entities.user.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(description = "Datos del usuario.")
public class CreateUserResponseDto {

  @Schema(
      name = "userName",
      description = "detail: Nombre de usuario",
      example = "rodri_bangu")
  public String userName;

  @Schema(
      name = "role",
      description = "detail: Rol del usuario",
      example = "admin")
  public RoleType role;

}
