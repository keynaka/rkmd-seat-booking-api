package com.rkmd.toki_no_nagare.dto.user;

import com.rkmd.toki_no_nagare.entities.user.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Datos del usuario.")
public class GetUserResponseDto {

  @Schema(
      name = "userName",
      description = "detail: Nombre de usuario",
      example = "tiki_taka")
  private String userName;

  @Schema(
      name = "role",
      description = "detail: Role del usuario",
      example = "viewer")
  private RoleType role;

}
