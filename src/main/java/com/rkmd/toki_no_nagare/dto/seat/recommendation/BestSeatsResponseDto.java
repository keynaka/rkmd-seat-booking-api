package com.rkmd.toki_no_nagare.dto.seat.recommendation;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BestSeatsResponseDto {
    @Schema(
            name = "score",
            description = "detail: Puntaje del combo",
            example = "1.27",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Double score;

    @Schema(
            name = "combo",
            description = "detail: Lista de seats que corresponden al combo",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public List<Seat> combo;
}
