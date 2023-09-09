package com.rkmd.toki_no_nagare.unit;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.service.SeatService;
import com.rkmd.toki_no_nagare.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rkmd.toki_no_nagare.utils.Constants.TOTAL_SEATS;

@SpringBootTest
@TestPropertySource(properties = {
        "PAYMENT_MP_ACCOUNT=test",
        "EVENT_ADDRESS=test",
        "EVENT_DATE=test",
        "EVENT_TIME=test",
        "EVENT_NAME=test",
        "EVENT_PLACE=test",
        "MAIL_USERNAME=test",
        "MAIL_PASSWORD=test",
        "MAIL_HEADER_IMAGE_CANONICAL_PATH=test",
        "SWAGGER_ENABLE=test",
        "DB_PORT=3600",
        "DB_HOST=test",
        "DB_NAME=test",
        "DB_CONNECTION_PARAMS=test",
        "JOB_CRON=0 0 0 * * *",
        "MAIL_BACKUP_RECIPIENT1=test",
        "MAIL_BACKUP_RECIPIENT2=test",
        "MAIL_BACKUP_RECIPIENT3=test",
})
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
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());

        Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(SeatSector.PLATEA, SeatStatus.VACANT);
        int comboSize = 5;
        int comboCount = 4;

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);

        Assertions.assertTrue(topCombosByRow.containsKey(12L));
        Assertions.assertTrue(topCombosByRow.containsKey(13L));
        Assertions.assertTrue(topCombosByRow.containsKey(Constants.PLATEA_BEST_ROW.longValue()));
        Assertions.assertTrue(topCombosByRow.containsKey(15L));

        Assertions.assertEquals(5, ((List) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).size());
        Assertions.assertEquals(4, ((List<Seat>) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).get(0).getColumn());
        Assertions.assertEquals(2, ((List<Seat>) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).get(1).getColumn());
        Assertions.assertEquals(1, ((List<Seat>) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).get(2).getColumn());
        Assertions.assertEquals(3, ((List<Seat>) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).get(3).getColumn());
        Assertions.assertEquals(5, ((List<Seat>) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("combo")).get(4).getColumn());

        Assertions.assertEquals(1.3333333333333333, ((Double) topCombosByRow.get(Constants.PLATEA_BEST_ROW.longValue()).get("score")));
    }

    @Test
    public void testReserveAllTheaterSeats() {
        int MAX_COLUMN_SIZE = 32;
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());

        for (SeatSector sector : List.of(SeatSector.PLATEA, SeatSector.PULLMAN)) {
            for (int i = 0 ; i < MAX_COLUMN_SIZE ; i++) {
                Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
                int comboSize = 1;
                int comboCount = 24; // To simulate that all rows are offered and reserved by the client. Each iteration reserves the whole column

                Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
                for (Map<String, Object> bestRowCombo : topCombosByRow.values()) {
                    seatService.updateSeatStatus(((List<Seat>) bestRowCombo.get("combo")).get(0), SeatStatus.RESERVED);
                }

                // First recommendations should be on the column 1L because they are in the middle
                if (i == 0) Assertions.assertTrue(((List<Seat>) topCombosByRow.get(1L).get("combo")).get(0).getColumn().equals(1L));

                // On last iteration, the recommendations should be the columns 32
                if (i == 31 && sector.equals(SeatSector.PLATEA)) {
                    Assertions.assertEquals(10, topCombosByRow.size());
                    Assertions.assertTrue(((List<Seat>) topCombosByRow.get(5L).get("combo")).get(0).getColumn().equals(32L));
                }

                // On penultimate iteration, the recommendations should be the columns 32 because on pullman the column 31 is missing
                if (i == 30 && sector.equals(SeatSector.PULLMAN)) {
                    Assertions.assertEquals(8, topCombosByRow.size());
                    Assertions.assertTrue(((List<Seat>) topCombosByRow.get(3L).get("combo")).get(0).getColumn().equals(32L));
                }
            }
        }

        List<Seat> vacantSeats = seatService.getSeatsOfStatus(SeatStatus.VACANT);
        Assertions.assertTrue(vacantSeats.isEmpty());
    }

    @Test
    public void testTryingToReserveWith16ComboSizeShouldLeaveVacantSeats() {
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());

        for (SeatSector sector : List.of(SeatSector.PLATEA, SeatSector.PULLMAN)) {
            for (int i = 0 ; i < 2 ; i++) {
                Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
                int comboSize = 16;
                int comboCount = 24; // To simulate that all rows are offered and reserved by the client. Each iteration reserves the whole column

                Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
                for (Map<String, Object> bestRowCombo : topCombosByRow.values()) {
                    for (Seat seat : ((List<Seat>) bestRowCombo.get("combo"))) {
                        seatService.updateSeatStatus(seat, SeatStatus.RESERVED);
                    }
                }

                if (i == 1) {
                    Assertions.assertTrue(topCombosByRow.isEmpty());
                }
            }
        }

        List<Seat> vacantSeats = seatService.getSeatsOfStatus(SeatStatus.VACANT);
        Assertions.assertTrue(!vacantSeats.isEmpty());
    }

    @Test
    public void testReserving5PULLMANFullColumnsInTheMiddleAndReleasing1RowShouldRecommendInTheMiddleAgain() {
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());
        SeatSector sector = SeatSector.PULLMAN;

        Map<Long, List<Seat>> pullmanSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        int comboSize = 5;
        int comboCount = 24; // To simulate that all rows are offered and reserved by the client. Each iteration reserves the whole column

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(pullmanSeatsByRow, comboSize, comboCount);
        for (Map.Entry<Long, Map<String, Object>> bestRowCombo : topCombosByRow.entrySet()) {
            if (!bestRowCombo.getKey().equals(1L)) {
                for (Seat seat : ((List<Seat>) bestRowCombo.getValue().get("combo"))) {
                    seatService.updateSeatStatus(seat, SeatStatus.RESERVED);
                }
            }
        }

        pullmanSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        // We didn't reserve the 1st row, so the bestOption should be that row
        comboCount = 1;
        topCombosByRow = seatService.searchTopCombosByRow(pullmanSeatsByRow, comboSize, comboCount);

        Assertions.assertTrue(topCombosByRow.containsKey(Constants.PULLMAN_BEST_ROW.longValue()));
    }

    @Test
    public void testReserving5PLATEAFullColumnsInTheMiddleAndReleasing1stRowShouldStillRecommendPlateaBestRow() {
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());
        SeatSector sector = SeatSector.PLATEA;

        Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        int comboSize = 5;
        int comboCount = 24; // To simulate that all rows are offered and reserved by the client. Each iteration reserves the whole column

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
        for (Map.Entry<Long, Map<String, Object>> bestRowCombo : topCombosByRow.entrySet()) {
            if (!bestRowCombo.getKey().equals(1L)) {
                for (Seat seat : ((List<Seat>) bestRowCombo.getValue().get("combo"))) {
                    seatService.updateSeatStatus(seat, SeatStatus.RESERVED);
                }
            }
        }

        plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        // We didn't reserve the 1st row, so the bestOption should be that row
        comboCount = 1;
        topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);

        // Even if first row middle is free, it will still recommend the platea best row because has better scoring not being in the middle
        Assertions.assertTrue(topCombosByRow.containsKey(Constants.PLATEA_BEST_ROW.longValue()));
    }

    @Test
    public void testReserving5PLATEAFullColumnsInTheMiddleAndReleasing4SeatsOfPlateaBestRowAndRequesting3ShouldGiveMeMiddleSeatsAndLeave1Seat() {
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());
        SeatSector sector = SeatSector.PLATEA;

        Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        int comboSize = 5;
        int comboCount = 1;

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
        for (Map.Entry<Long, Map<String, Object>> bestRowCombo : topCombosByRow.entrySet()) {
            List<Seat> seats = ((List<Seat>) bestRowCombo.getValue().get("combo"));
            seatService.updateSeatStatus(seats.get(0), SeatStatus.RESERVED); // FIRST ITERATION: Only reserve the 1st of the 5 seats
            seatService.updateSeatStatus(seats.get(seats.size()-1), SeatStatus.RESERVED); // FIRST ITERATION: Only reserve the 5th of the 5 seats
        }

        plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        comboSize = 2;
        topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);

        Assertions.assertTrue(topCombosByRow.containsKey(Constants.PLATEA_BEST_ROW.longValue()));

        for (Map.Entry<Long, Map<String, Object>> bestRowCombo : topCombosByRow.entrySet()) {
            for (Seat seat : ((List<Seat>) bestRowCombo.getValue().get("combo"))) {
                seatService.updateSeatStatus(seat, SeatStatus.RESERVED); // SECOND ITERATION
            }
        }

        List<Seat> plateaBestRow = seatService.getSectorSeatsByRow(sector, null).get(Constants.PLATEA_BEST_ROW.longValue());
        Assertions.assertTrue(plateaBestRow.get(14).getColumn().equals(4L) && plateaBestRow.get(14).getStatus().equals(SeatStatus.RESERVED)); //The column 4 is reserved at FIRST ITERATION
        Assertions.assertTrue(plateaBestRow.get(15).getColumn().equals(2L) && plateaBestRow.get(15).getStatus().equals(SeatStatus.RESERVED)); //The column 2 is reserved at SECOND ITERATION
        Assertions.assertTrue(plateaBestRow.get(16).getColumn().equals(1L) && plateaBestRow.get(16).getStatus().equals(SeatStatus.RESERVED)); //The column 1 is reserved at SECOND ITERATION
        Assertions.assertTrue(plateaBestRow.get(17).getColumn().equals(3L) && plateaBestRow.get(17).getStatus().equals(SeatStatus.VACANT)); //The column 3 is vacant (1 seat space)
        Assertions.assertTrue(plateaBestRow.get(18).getColumn().equals(5L) && plateaBestRow.get(18).getStatus().equals(SeatStatus.RESERVED)); //The column 5 is reserved at FIRST ITERATION

        plateaSeatsByRow = seatService.getSectorSeatsByRow(sector, SeatStatus.VACANT);
        comboCount = 4; //We need to request more combos because column 1 of other rows have better scoring than the column 3 of the PLATEA_BEST_ROW
        comboSize = 1;
        topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);

        Assertions.assertTrue(topCombosByRow.containsKey(Constants.PLATEA_BEST_ROW.longValue()));

        for (Map.Entry<Long, Map<String, Object>> bestRowCombo : topCombosByRow.entrySet()) {
            for (Seat seat : ((List<Seat>) bestRowCombo.getValue().get("combo"))) {
                seatService.updateSeatStatus(seat, SeatStatus.RESERVED); // THIRD ITERATION
            }
        }

        plateaBestRow = seatService.getSectorSeatsByRow(sector, null).get(Constants.PLATEA_BEST_ROW.longValue());
        Assertions.assertTrue(plateaBestRow.get(14).getColumn().equals(4L) && plateaBestRow.get(14).getStatus().equals(SeatStatus.RESERVED)); //The column 4 is reserved at FIRST ITERATION
        Assertions.assertTrue(plateaBestRow.get(15).getColumn().equals(2L) && plateaBestRow.get(15).getStatus().equals(SeatStatus.RESERVED)); //The column 2 is reserved at SECOND ITERATION
        Assertions.assertTrue(plateaBestRow.get(16).getColumn().equals(1L) && plateaBestRow.get(16).getStatus().equals(SeatStatus.RESERVED)); //The column 1 is reserved at SECOND ITERATION
        Assertions.assertTrue(plateaBestRow.get(17).getColumn().equals(3L) && plateaBestRow.get(17).getStatus().equals(SeatStatus.RESERVED)); //The column 3 is reserved at THIRD ITERATION
        Assertions.assertTrue(plateaBestRow.get(18).getColumn().equals(5L) && plateaBestRow.get(18).getStatus().equals(SeatStatus.RESERVED)); //The column 5 is reserved at FIRST ITERATION

    }

    @Test
    public void testShuffleRecommendations() {
        Assertions.assertEquals(TOTAL_SEATS, seatService.bootstrapTheaterSeats());

        Map<Long, List<Seat>> plateaSeatsByRow = seatService.getSectorSeatsByRow(SeatSector.PLATEA, SeatStatus.VACANT);
        int comboSize = 5;
        int comboCount = 9;

        Map<Long, Map<String, Object>> topCombosByRow = seatService.searchTopCombosByRow(plateaSeatsByRow, comboSize, comboCount);
        Map<Long, Map<String, Object>> shuffledRecommendations = seatService.shuffleRecommendations(topCombosByRow);

        Assertions.assertTrue(!shuffledRecommendations.isEmpty());

        shuffledRecommendations = seatService.shuffleRecommendations(topCombosByRow);

        Assertions.assertTrue(!shuffledRecommendations.isEmpty());
    }
}
