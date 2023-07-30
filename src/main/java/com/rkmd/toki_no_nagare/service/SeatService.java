package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public Optional<Seat> getSeat(String row, String column) {
        SeatId id = new SeatId(row, column);
        return seatRepository.findById(id);
    }

    public Seat createSeat(Map<String, String> json) {
        if (seatRepository.findById(new SeatId(json.get("row"), json.get("column"))).isPresent())
            throw new BadRequestException("seat_already_exists", "This seat already exists");

        Seat newSeat = new Seat();
        newSeat.setRow(json.get("row"));
        newSeat.setColumn(json.get("column"));
        newSeat.setStatus(SeatStatus.valueOf(json.get("status").toUpperCase()));
        newSeat.setBooking(null);

        try {
            return seatRepository.save(newSeat);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }
}