package com.rkmd.toki_no_nagare.dto.seat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Schema(description = "Datos de la butaca")
public class PrereserveInputDto {

    @Schema(
            name = "seats",
            description = "detail: List of seats",
            example = "[{row: '1', column: '1', sector: 'platea'}]")
    private List<SeatRequestDto> seats;
}
