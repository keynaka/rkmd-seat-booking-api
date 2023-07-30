package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.service.SeatService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/seat")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("/{column}/{row}")
    public ResponseEntity<Seat> getSeat(@PathVariable("row") String row, @PathVariable("column") String column) {
        Optional<Seat> seat = seatService.getSeat(row, column);
        if (!seat.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(seat.get());
    }

    @PostMapping("")
    public ResponseEntity<Seat> createSeat(@RequestBody @Valid Map<String, String> json) {
        ValidationUtils.checkParam(json.containsKey("row"), "row_missing", "Row is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("column"), "column_missing", "Column is missing and is mandatory");
        ValidationUtils.checkParam(json.containsKey("status"), "status_missing", "Status is missing and is mandatory");

        Seat newSeat = seatService.createSeat(json);

        return ResponseEntity.ok().body(newSeat);
    }

    /*
    * This endpoint's goal is just to bootstrap theater's Seat.
    * Returns total seat's count.
    */
    @PostMapping("/bootstrap")
    public ResponseEntity<Integer> bootstrapTheaterSeats(@RequestBody @Valid Map<String, String> json) {
        Seat newSeat = seatService.createSeat(json);

        return ResponseEntity.ok().body(List.of(newSeat).size());
    }
}