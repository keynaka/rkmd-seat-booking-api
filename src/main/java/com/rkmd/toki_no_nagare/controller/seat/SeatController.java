package com.rkmd.toki_no_nagare.controller.seat;

import com.rkmd.toki_no_nagare.dto.seat.PrereserveInputDto;
import com.rkmd.toki_no_nagare.dto.seat.PrereserveSeatDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatPricesBySectorDto;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.service.SeatService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public ResponseEntity<List<Seat>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();

        return ResponseEntity.ok().body(seats);
    }

    @GetMapping("/{sector}")
    public ResponseEntity<Map<Long, List<Seat>>> getSectorSeatsByRow(@PathVariable("sector") String sector,
                                                                     @RequestParam(name = "status", required = false) String status) {
        SeatSector seatSector = SeatSector.valueOf(sector.toUpperCase());
        SeatStatus seatStatus = status != null ? SeatStatus.valueOf(status.toUpperCase()) : null;
        Map<Long, List<Seat>> seats = seatService.getSectorSeatsByRow(seatSector, seatStatus);

        return ResponseEntity.ok().body(seats);
    }

    @PutMapping("/prereserve")
    public ResponseEntity<Boolean> prereserveSeat(@Valid @RequestBody PrereserveInputDto prereserveInputDto) {
        List<Seat> prereservedSeats = seatService.prereserveSeats(prereserveInputDto);

        return ResponseEntity.ok().body(prereservedSeats.size() == prereserveInputDto.getSeats().size());
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

        Map<Long, List<Seat>> seats = seatService.filterPrereservedSeats(seatService.getSectorSeatsByRow(seatSector, SeatStatus.VACANT));
        ValidationUtils.checkFound(!seats.isEmpty(), "seats_not_found", "There are no seats at the selected sector and row");

        Map<Long, Map<String, Object>> bestCombosByRow = seatService.searchTopCombosByRow(seats, comboSize, comboCount);

        return ResponseEntity.ok().body(bestCombosByRow);
    }

    @PutMapping("/prices/sector")
    public ResponseEntity<Void> setupSeatPricesBySector(@RequestBody SeatPricesBySectorDto request) {
        seatService.setSeatPricesBySector(request.getPullmanSeatPrices(), request.getPlateaSeatPrices());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/prices/seat")
    public ResponseEntity<Void> setupSeatPricesByRow() {
        seatService.setSeatPricesByRow();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}