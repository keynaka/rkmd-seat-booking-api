package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.SeatRepository;
import com.rkmd.toki_no_nagare.utils.Constants;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public Optional<Seat> getSeat(Long row, Long column, SeatSector sector) {
        SeatId id = new SeatId(row, column, sector);
        return seatRepository.findById(id);
    }
    public Map<Long, List<Seat>> getSeatsBySector(SeatSector seatSector) {
        Map<Long, List<Seat>> result = new HashMap<>();
        for (Seat seat : seatRepository.findAllBySector(seatSector)) {
            if (!result.containsKey(seat.getRow()))
                result.put(seat.getRow(), new ArrayList<>());

            result.get(seat.getRow()).add(seat);
        }

        return result;
    }
    public List<Seat> getSeatsBySectorAndRow(SeatSector seatSector, Long row) {
        return seatRepository.findAllBySectorAndRow(seatSector, row);
    }

    public Seat createSeat(SeatSector sector, Long row, Long column, SeatStatus status, Long auxiliarColumn) {
        SeatId seatId = new SeatId(row, column, sector);

        if (seatRepository.findById(seatId).isPresent())
            throw new BadRequestException("seat_already_exists", "This seat already exists");

        Seat newSeat = new Seat();
        newSeat.setSector(sector);
        newSeat.setRow(row);
        newSeat.setColumn(column);
        newSeat.setAuxiliarColumn(sector.equals(SeatSector.PALCOS) ? null : auxiliarColumn);
        newSeat.setStatus(status);
        newSeat.setBooking(null);

        try {
            return seatRepository.save(newSeat);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    public Seat updateSeat(Seat seat, SeatStatus updatedStatus) {
        seat.setStatus(updatedStatus);
        try {
            return seatRepository.save(seat);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    public Map<Long, List<Double>> searchBestOptions(Map<Long, List<Seat>> sectorSeats, int seatCount) {
        Map<Long, List<Double>> scores = new HashMap<>();
        for (Map.Entry<Long, List<Seat>> row : sectorSeats.entrySet()) {
            List<Double> s = List.of(
                    getSeatsComboScore(getMiddleSeats(row.getValue(), seatCount)),
                    getSeatsComboScore(getBorderSeats(row.getValue(), seatCount))
            );
            scores.put(row.getKey(), s);
        }

        return scores;
    }

    private static List<Seat> getMiddleSeats(List<Seat> seats, int seatCount) {
        ValidationUtils.checkFound(seats.size() > seatCount, "no_enough_seats_at_row", "There are no enough seats for this row");

        int startIndex = (seats.size() / 2) - (seatCount / 2);
        int endIndex = startIndex + seatCount;

        return seats.subList(startIndex, endIndex);
    }

    private static List<Seat> getBorderSeats(List<Seat> seats, int seatCount) {
        ValidationUtils.checkFound(seats.size() > seatCount, "no_enough_seats_at_row", "There are no enough seats for this row");

        int startIndex = 0;
        int endIndex = startIndex + seatCount;

        return seats.subList(startIndex, endIndex);
    }

    private static Double getSeatsComboScore(List<Seat> seats) {
        Double selectedSeatsColumnAvg = seats.stream()
                .map(seat -> seat.getColumn())
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        Double columnScore = 1.0 / (1.0 + Math.abs(selectedSeatsColumnAvg - 1.0));

        Double selectedSeatsRow = Double.valueOf(seats.get(0).getRow());


        Double rowScore = 1.0 / (1.0 + Math.abs(selectedSeatsRow - bestRowsBySector(seats)));

        return columnScore + rowScore;
    }

    private static Double bestRowsBySector(List<Seat> seats) {
        Double totalRowsCount = (double) Constants.THEATER_LAYOUT.get(seats.get(0).getSector()).size();
        Double result;
        switch (seats.get(0).getSector()) {
            case PLATEA:
                result = totalRowsCount / 2;
                break;
            default:
            case PULLMAN:
                result = 1.0;
                break;
        }

        return result;
    }
}