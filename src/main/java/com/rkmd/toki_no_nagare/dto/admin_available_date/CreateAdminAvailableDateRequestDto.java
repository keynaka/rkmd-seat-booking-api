package com.rkmd.toki_no_nagare.dto.admin_available_date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
@Schema(description = "Solicitud de fecha disponible para admin")
public class CreateAdminAvailableDateRequestDto {

    @Valid
    @NotNull(message = "El campo 'initDate' no puede ser nulo")
    public String initDate;

    @Valid
    @NotNull(message = "El campo 'initDate' no puede ser nulo")
    public String endDate;

    @Valid
    @NotNull(message = "El campo 'place' no puede ser nulo")
    public String place;

}