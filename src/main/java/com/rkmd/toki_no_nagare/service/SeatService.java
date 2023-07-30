package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
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

    public Optional<Seat> getSeat(Long row, Long column, SeatSector sector) {
        SeatId id = new SeatId(row, column, sector);
        return seatRepository.findById(id);
    }

    public Seat createSeat(Map<String, Object> json) {
        SeatId seatId = new SeatId(
                Long.valueOf((String) json.get("row")),
                Long.valueOf((String) json.get("column")),
                SeatSector.valueOf(((String) json.get("sector")).toUpperCase())
        );

        if (seatRepository.findById(seatId).isPresent())
            throw new BadRequestException("seat_already_exists", "This seat already exists");

        Seat newSeat = new Seat();
        newSeat.setSector(SeatSector.valueOf(((String) json.get("sector")).toUpperCase()));
        newSeat.setRow(Long.valueOf((String) json.get("row")));
        newSeat.setColumn(Long.valueOf((String) json.get("column")));
        newSeat.setStatus(SeatStatus.valueOf(((String) json.get("status")).toUpperCase()));
        newSeat.setBooking(null);

        try {
            return seatRepository.save(newSeat);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }
}