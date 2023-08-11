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
import java.util.stream.Collectors;

@Service
public class SeatService {
    public static final double BEST_COLUMN_POSITION = 1.0;
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

    /*
    * This method looks for the best combos to recommend to the client. It will select the best <comboCount> combos of
    * different rows that have <comboSize> consecutive seats.
    * If the <comboCount> is greater that the available count of rows, it will recommend the best combos of each row
    *
    * If there are no <comboSize> consecutive seats in any row, it will return empty
    * Ex.
    *   comboSize = 50
    *   There are no rows containing more than 32 seats, so it won't recommend any seats
    * */
    public Map<Long, Map<String, Object>> searchTopCombosByRow(Map<Long, List<Seat>> sectorSeats, int comboSize, int comboCount) {
        Map<Long, Map<String, Object>> bestComboByRow = getBestComboByRow(sectorSeats, comboSize);

        return filterTopCombos(comboCount, bestComboByRow);
    }

    /*
    * This method search the best combo of each Row. It will return the score and the list of seats that represent
    * the combo
    * */
    private static Map<Long, Map<String, Object>> getBestComboByRow(Map<Long, List<Seat>> sectorSeats, int comboSize) {
        Map<Long, Map<String, Object>> bestComboByRow = new HashMap<>();
        for (Map.Entry<Long, List<Seat>> row : sectorSeats.entrySet()) {
            List<List<Seat>> combos = findCombosAvailable(row.getValue(), comboSize);
            if (!combos.isEmpty()) {
                Double maxScore = Double.valueOf(0);
                List<Seat> selectedRowCombo = new ArrayList<>();
                for (List<Seat> combo : combos) {
                    Double score = getSeatsComboScore(combo);
                    if (score > maxScore) {
                        selectedRowCombo = combo;
                        maxScore = score;
                    }
                }
                bestComboByRow.put(row.getKey(), Map.of("score", maxScore, "combo", selectedRowCombo));
            }
        }
        return bestComboByRow;
    }

    /*
    * This method filters just the top <comboCount> combos of <allCombos>. Each of these combos contain the list of seats
    * and the score associated
    * */
    private static Map<Long, Map<String, Object>> filterTopCombos(int comboCount, Map<Long, Map<String, Object>> allCombos) {
        Map<Long, Map<String, Object>> top = allCombos.entrySet().stream()
                .sorted((e1, e2) -> Double.compare((Double) e2.getValue().get("score"), (Double) e1.getValue().get("score")))
                .limit(comboCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return top;
    }

    /*
    * This method looks for all the consecutive seats available for the <comboSize>
    * */
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

    /*
    * This method checks if the list of seats are consecutive
    * */
    public static boolean isConsecutive(List<Seat> seats) {
        for (int i = 1; i < seats.size(); i++) {
            if (seats.get(i).getAuxiliarColumn() - seats.get(i - 1).getAuxiliarColumn() != 1) {
                return false;
            }
        }
        return true;
    }

    /*
     * This method returns the score of a seat considering the seat's columns avg and the row selected taking in count
     * the sector too
     * */
    private static Double getSeatsComboScore(List<Seat> seats) {
        Double selectedSeatsColumnAvg = seats.stream()
                .map(seat -> seat.getColumn())
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
        Double columnScore = 1.0 / (1.0 + Math.abs(selectedSeatsColumnAvg - BEST_COLUMN_POSITION));

        Double rowScore = 1.0 / (1.0 + Math.abs(Double.valueOf(seats.get(0).getRow()) - bestRowsBySector(seats)));

        return columnScore + rowScore;
    }

    /*
    * When we are looking for Platea seats we set that middle rows are the best options.
    * If we are looking for Pullman seats, then the first seats are the best options.
    * */
    private static Double bestRowsBySector(List<Seat> seats) {
        Double result = 1.0;
        if (seats.get(0).getSector().equals(SeatSector.PLATEA)) {
            Double totalRowsCount = (double) Constants.THEATER_LAYOUT.get(seats.get(0).getSector()).size();
            result = totalRowsCount / 2;
        }

        return result;
    }

    // TODO: Delete later. Just to analyze combos scores
    public Map<Long, Map<String, Map<String, Object>>> searchBestCombosData(Map<Long, List<Seat>> sectorSeats, int comboSize) {
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
}