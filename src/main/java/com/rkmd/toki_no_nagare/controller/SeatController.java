package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.service.SeatService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("")
    public ResponseEntity<Seat> getSeat(@RequestParam(name = "row") Long row,
                                        @RequestParam(name = "column") Long column,
                                        @RequestParam(name = "sector") String sector) {
        Optional<Seat> seat = seatService.getSeat(row, column, SeatSector.valueOf(sector.toUpperCase()));
        ValidationUtils.checkFound(seat.isPresent(), "seat_not_found", "Seat not found");

        return ResponseEntity.ok().body(seat.get());
    }

    @PutMapping("/{sector}/{row}/{column}/{status}")
    public ResponseEntity<Seat> updateSeat(@PathVariable("sector") String sector,
                                           @PathVariable("row") Long row,
                                           @PathVariable("column") Long column,
                                           @PathVariable("status") String status) {
        SeatSector seatSector;
        SeatStatus newSeatStatus;
        try {
            seatSector = SeatSector.valueOf(sector.toUpperCase());
            newSeatStatus = SeatStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid_sector_value", "Invalid sector or status");
        }

        Optional<Seat> seat = seatService.getSeat(row, column, seatSector);
        ValidationUtils.checkFound(seat.isPresent(), "seat_not_found", "Seat not found");

        Seat updatedSeat = seatService.updateSeatStatus(seat.get(), newSeatStatus);

        return ResponseEntity.ok().body(updatedSeat);
    }

    /*
    * This endpoint's goal is just to bootstrap theater's Seat.
    * Returns total seat's count.
    */
    @PostMapping("/bootstrap")
    public ResponseEntity<Integer> bootstrapTheaterSeats() {
        return ResponseEntity.ok().body(seatService.bootstrapTheaterSeats());
    }

    @GetMapping("/recommendation")
    public ResponseEntity<Map<Long, Map<String, Object>>> getRecommendedSeats(
            @RequestParam(name = "combo_count") int comboCount,
            @RequestParam(name = "combo_size") int comboSize,
            @RequestParam(name = "sector") String sector) {
        SeatSector seatSector;
        try {
            seatSector = SeatSector.valueOf(sector.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid_sector_value", "Invalid sector or status");
        }

        Map<Long, List<Seat>> seats = seatService.getSectorSeatsByRow(seatSector, SeatStatus.VACANT);
        ValidationUtils.checkFound(!seats.isEmpty(), "seats_not_found", "There are no seats at the selected sector and row");

        //TODO: Delete later This method is used just for Testing scores of all seats
        //Map<Long, Map<String, Map<String, Object>>> scores = seatService.searchBestCombosData(seats, comboSize);

        Map<Long, Map<String, Object>> bestCombosByRow = seatService.searchTopCombosByRow(seats, comboSize, comboCount);

        return ResponseEntity.ok().body(bestCombosByRow);
    }
}