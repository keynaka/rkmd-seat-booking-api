package com.rkmd.toki_no_nagare.dto.Contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del contacto")
public class ContactRequestDto {

  @Schema(
      name = "dni",
      description = "detail: Número de documento (sin puntos y sin espacios) ",
      example = "11325689",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long dni;

  @Schema(
      name = "name",
      description = "detail: Nombre",
      example = "Cristian Pablo Rodrigo Alejandro",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Schema(
      name = "lastName",
      description = "detail: Apellido",
      example = "Higa",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String lastName;

  @Schema(
      name = "email",
      description = "detail: Dirección de correo electrónico",
      example = "direccion_de_mail@gmail.com",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Schema(
      name = "phone",
      description = "detail: Número de telefono fijo o celular",
      example = "1132533689",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String phone;

}
