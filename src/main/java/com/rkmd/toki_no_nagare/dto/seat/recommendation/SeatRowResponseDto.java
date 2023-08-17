package com.rkmd.toki_no_nagare.dto.seat.recommendation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatRowResponseDto {
    @Schema(
            name = "row",
            description = "detail: Fila del asiento",
            example = "1L",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Long row;

    //TODO: Ver si esto tiene sentido parece medio complicado para hacer el get usandolo como key
}
