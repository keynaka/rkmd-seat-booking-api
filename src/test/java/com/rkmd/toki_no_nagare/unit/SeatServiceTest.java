package com.rkmd.toki_no_nagare.unit;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.service.SeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class SeatServiceTest {
    @Autowired
    private SeatService seatService;

    @BeforeEach
    public void init() {
        seatService.clearSeats();
    }

    @Test
    public void testCreateSeatAndThrowBadRequestIfAlreadyExists() {
        Seat seat = seatService.createSeat(SeatSector.PLATEA, 1L, 1L, SeatStatus.VACANT, 10);

        Assertions.assertEquals(SeatSector.PLATEA, seat.getSector());
        Assertions.assertEquals(1L, seat.getRow());
        Assertions.assertEquals(1L, seat.getColumn());
        Assertions.assertEquals(SeatStatus.VACANT, seat.getStatus());
        Assertions.assertEquals(10, seat.getAuxiliarColumn());

        Assertions.assertThrows(
                BadRequestException.class,
                ()-> seatService.createSeat(SeatSector.PLATEA, 1L, 1L, SeatStatus.VACANT, 10)
        );
    }

    @Test
    public void testGetSeatAndUpdateSeat() {
        seatService.createSeat(SeatSector.PLATEA, 1L, 1L, SeatStatus.VACANT, 10);

        Optional<Seat> seat = seatService.getSeat(1L, 1L, SeatSector.PLATEA);
        seatService.updateSeatStatus(seat.get(), SeatStatus.RESERVED);
        Assertions.assertEquals(1L, seat.get().getRow());
        Assertions.assertEquals(1L, seat.get().getColumn());
        Assertions.assertEquals(SeatStatus.RESERVED, seat.get().getStatus());
        Assertions.assertEquals(10, seat.get().getAuxiliarColumn());
    }

    @Test
    public void testSearchTopCombosByRow() {
        Assertions.assertEquals(1032, seatService.bootstrapTheaterSeats());

        Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(SeatSector.PLATEA);
        int comboSize = 5;
        int comboCount = 4;

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);

        Assertions.assertTrue(topCombosByRow.containsKey(10L));
        Assertions.assertTrue(topCombosByRow.containsKey(11L));
        Assertions.assertTrue(topCombosByRow.containsKey(12L));
        Assertions.assertTrue(topCombosByRow.containsKey(13L));

        Assertions.assertEquals(5, ((List) topCombosByRow.get(12L).get("combo")).size());
        Assertions.assertEquals(4, ((List<Seat>) topCombosByRow.get(12L).get("combo")).get(0).getColumn());
        Assertions.assertEquals(2, ((List<Seat>) topCombosByRow.get(12L).get("combo")).get(1).getColumn());
        Assertions.assertEquals(1, ((List<Seat>) topCombosByRow.get(12L).get("combo")).get(2).getColumn());
        Assertions.assertEquals(3, ((List<Seat>) topCombosByRow.get(12L).get("combo")).get(3).getColumn());
        Assertions.assertEquals(5, ((List<Seat>) topCombosByRow.get(12L).get("combo")).get(4).getColumn());

        Assertions.assertEquals(1.3333333333333333, ((Double) topCombosByRow.get(12L).get("score")));
    }

    @Test
    public void testReserveAllTheaterSeats() {
        int MAX_COLUMN_SIZE = 32;
        Assertions.assertEquals(1032, seatService.bootstrapTheaterSeats());

        for (SeatSector sector : List.of(SeatSector.PLATEA, SeatSector.PULLMAN, SeatSector.PALCOS)) {
            for (int i = 0 ; i < MAX_COLUMN_SIZE ; i++) {
                Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(sector);
                int comboSize = 1;
                int comboCount = 24;

                Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
                for (Map<String, Object> bestRowCombo : topCombosByRow.values()) {
                    seatService.updateSeatStatus(((List<Seat>) bestRowCombo.get("combo")).get(0), SeatStatus.RESERVED);
                }
            }
        }
        List<Seat> vacantSeats = seatService.getSeatsOfStatus(SeatStatus.VACANT);

        Assertions.assertTrue(vacantSeats.isEmpty());
    }
}
