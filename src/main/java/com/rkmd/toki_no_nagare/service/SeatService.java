package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.SeatRepository;
import com.rkmd.toki_no_nagare.utils.Constants;
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

    public Seat createSeat(SeatSector sector, Long row, Long column, SeatStatus status, Integer auxiliarColumn) {
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

    public Map<Long, Map<String, Map<String, Object>>> filterTopBestCombos( Map<Long, Map<String, Map<String, Object>>> searchBestCombos, int topSize) {
        return searchBestCombos; //TODO: Continue here to filter top <topSize> scoring combo seats
    }

    public Map<Long, Map<String, Map<String, Object>>> searchBestCombos(Map<Long, List<Seat>> sectorSeats, int comboSize) {
        Map<Long, Map<String, Map<String, Object>>> scores = new HashMap<>();
        for (Map.Entry<Long, List<Seat>> row : sectorSeats.entrySet()) {
            List<List<Seat>> combos = findCombosAvailable(row.getValue(), comboSize);
            Map<String, Map<String, Object>> rowAvailableCombos = new HashMap<>();
            for (List<Seat> combo : combos) {
                String comboMinMaxColumns = String.format("%d-%d", combo.get(0).getAuxiliarColumn(), combo.get(comboSize-1).getAuxiliarColumn());
                rowAvailableCombos.put(
                        comboMinMaxColumns, Map.of(
                                "score", getSeatsComboScore(combo),
                                "seats", combo
                        )
                );
            }
            scores.put(row.getKey(), rowAvailableCombos);
        }

        return scores;
    }

    public static List<List<Seat>> findCombosAvailable(List<Seat> seats, int comboSize) {
        List<List<Seat>> combos = new ArrayList<>();

        for (int i = 0; i <= seats.size() - comboSize; i++) {
            List<Seat> subsequence = seats.subList(i, i + comboSize);
            if (isConsecutive(subsequence)) {
                combos.add(new ArrayList<>(subsequence));
            }
        }

        return combos;
    }

    public static boolean isConsecutive(List<Seat> seats) {
        for (int i = 1; i < seats.size(); i++) {
            if (seats.get(i).getAuxiliarColumn() - seats.get(i - 1).getAuxiliarColumn() != 1) {
                return false;
            }
        }
        return true;
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