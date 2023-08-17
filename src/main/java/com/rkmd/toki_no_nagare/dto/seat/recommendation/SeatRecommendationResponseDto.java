package com.rkmd.toki_no_nagare.dto.seat.recommendation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Seats recomendados")
public class SeatRecommendationResponseDto {

    @Schema(
            name = "bestSeatsByRow",
            description = "detail: Best seats by row",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Map<Long, BestSeatsResponseDto> bestSeatsByRow;
}
