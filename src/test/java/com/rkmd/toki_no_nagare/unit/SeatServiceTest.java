package com.rkmd.toki_no_nagare.unit;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.service.SeatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SeatServiceTest {
    @Autowired
    private SeatService seatService;

    @Test
    public void testCreateSeat() {
        Seat seat = seatService.createSeat(SeatSector.PLATEA, 1L, 1L, SeatStatus.VACANT, 10);

        Assertions.assertEquals(SeatSector.PLATEA, seat.getSector());
        Assertions.assertEquals(1L, seat.getRow());
        Assertions.assertEquals(1L, seat.getColumn());
        Assertions.assertEquals(SeatStatus.VACANT, seat.getStatus());
        Assertions.assertEquals(10, seat.getAuxiliarColumn());
    }

    @Test
    public void testUpdateSeat() {
        Seat seat = seatService.createSeat(SeatSector.PLATEA, 2L, 1L, SeatStatus.VACANT, 10);

        seatService.updateSeat(seat, SeatStatus.RESERVED);
        Assertions.assertEquals(2L, seat.getRow());
        Assertions.assertEquals(1L, seat.getColumn());
        Assertions.assertEquals(SeatStatus.RESERVED, seat.getStatus());
        Assertions.assertEquals(10, seat.getAuxiliarColumn());
    }
}
