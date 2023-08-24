package com.rkmd.toki_no_nagare.service;

import com.google.common.annotations.VisibleForTesting;
import com.rkmd.toki_no_nagare.dto.seat.SeatRequestDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import com.rkmd.toki_no_nagare.entities.seat.SeatStatus;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.SeatRepository;
import com.rkmd.toki_no_nagare.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.rkmd.toki_no_nagare.utils.Constants.THEATER_LAYOUT;
import static com.rkmd.toki_no_nagare.utils.SeatPrices.SEAT_PRICES;

@Service
public class SeatService {
    private List<Seat> theaterSeats;
    public static final double BEST_COLUMN_POSITION = 1.0;
    @Autowired
    private SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
        this.theaterSeats = new ArrayList<>();
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }
    public Optional<Seat> getSeat(Long row, Long column, SeatSector sector) {
        SeatId id = new SeatId(row, column, sector);
        return seatRepository.findById(id);
    }
    public Map<Long, List<Seat>> getSectorSeatsByRow(SeatSector seatSector, SeatStatus seatStatus) {
        Map<Long, List<Seat>> result = new HashMap<>();
        List<Seat> seats = seatStatus != null ?
                seatRepository.findAllBySectorAndStatus(seatSector, seatStatus) :
                seatRepository.findAllBySector(seatSector);

        for (Seat seat : seats) {
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
        newSeat.setAuxiliarColumn(sector.equals(SeatSector.PALCOS) ? null : auxiliarColumn); //TODO: Check if PALCOS part should be included on recommendations logic
        newSeat.setStatus(status);
        newSeat.setBooking(null);

        try {
            return seatRepository.save(newSeat);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }

    public List<Seat> getSeatsOfStatus(SeatStatus seatStatus) {
        return seatRepository.findAllByStatus(seatStatus);
    }

    public Seat updateSeatStatus(Seat seat, SeatStatus updatedStatus) {
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
    private static boolean isConsecutive(List<Seat> seats) {
        for (int i = 1; i < seats.size(); i++) {
            if (seats.get(0).getAuxiliarColumn() == null) return false; //TODO: Check if PALCOS part should be included on recommendations logic

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
        return seats.get(0).getSector().equals(SeatSector.PLATEA) ? Constants.PLATEA_BEST_ROW : Constants.PULLMAN_BEST_ROW;
    }

    public int bootstrapTheaterSeats() {
        for(SeatSector sector : THEATER_LAYOUT.keySet()) {
            for(Long row : THEATER_LAYOUT.get(sector).keySet()) {
                Integer auxiliarColumn = 1;
                for(Long column : THEATER_LAYOUT.get(sector).get(row)) {
                    theaterSeats.add(createSeat(sector, row, column, SeatStatus.VACANT, auxiliarColumn));
                    auxiliarColumn ++;
                }
            }
        }

        return this.theaterSeats.size();
    }

    @VisibleForTesting
    public void clearSeats() {
        seatRepository.deleteAll();
        this.theaterSeats.clear();
    }

    /** This method sets the seat prices by sector. It receives as arguments the prices of each sector.
     * @param pullmanSeatPrices The price for 'PULLMAN' sector
     * @param palcoSeatPrices The price for 'PALCO' sector
     * @param plateaSeatPrices The price for 'PLATEA' sector
     * */
    public void setSeatPricesBySector(BigDecimal pullmanSeatPrices, BigDecimal palcoSeatPrices, BigDecimal plateaSeatPrices){
        List<Seat> seats = seatRepository.findAll();

        for (Seat seat : seats){
            if(seat.getPrice() == null){
                switch (seat.getSector()) {
                    case PULLMAN -> seat.setPrice(pullmanSeatPrices);
                    case PALCOS -> seat.setPrice(palcoSeatPrices);
                    case PLATEA -> seat.setPrice(plateaSeatPrices);
                }
                seatRepository.save(seat);
            }
        }
    }


    /** This method sets the seat prices per row, according to the prices defined in the utils.SeatPrices */
    public void setSeatPricesByRow(){
        for(SeatSector sector : THEATER_LAYOUT.keySet()) {
            for(Long row : THEATER_LAYOUT.get(sector).keySet()) {
                for(Long column : THEATER_LAYOUT.get(sector).get(row)) {
                    SeatId id = new SeatId(row, column, sector);
                    Optional<Seat> seat = seatRepository.findById(id);
                    seat.ifPresent(s -> {
                        seat.get().setPrice(SEAT_PRICES.get(sector).get(row));
                        seatRepository.save(seat.get());
                    });
                }
            }
        }
    }


    /** This method returns the seats requested by the user from the database.
     * @param request Seats requested by the user
     * @return List<Seat>
     * */
    public List<Seat> getSeatsRequestedByUser(List<SeatRequestDto> request){
        List<Seat> allSeats = seatRepository.findAll();
        List<Seat> seats = new ArrayList<>();

        for(SeatRequestDto seatRequested : request){
            seats.add(allSeats.stream()
                .filter(s -> s.getSector().equals(seatRequested.getSector()) && s.getRow().equals(seatRequested.getRow()) && s.getColumn().equals(seatRequested.getColumn()))
                .findFirst()
                .get());
        }
        return seats;
    }

    /** Validates the seat status
     * @param seats Seats requested by the user
     * @throws BadRequestException When the seat is not equals to seat status requested
     * */
    public void validateSeatsStatus(List<Seat> seats, SeatStatus seatStatus){
        for(Seat seat : seats){
            if(!seat.getStatus().equals(seatStatus)){
                throw new BadRequestException("invalid_seat", String.format("The seats are not %s", seatStatus.name()));
            }
        }
    }

    /** Calculates the sum of the prices of the requested seats
     * @param seats Seats requested by the user
     * @return BigDecimal Payment amounts
     * */
    public BigDecimal getTotalPaymentAmount(List<Seat> seats){
        return seats.stream().map(Seat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Updates the status of a seat to reserved
     * @param seats Seats requested by the user
     * */
    public void updateSeatStatus(List<Seat> seats){
        for(Seat seat : seats){
            seat.setStatus(SeatStatus.RESERVED);
            seatRepository.save(seat);
        }
    }


    /** This method associates the seat data with the booking, changes the seat's status and persists it in the database
     * @param seats Seats requested by the user
     * @param booking Booking data
     * */
    public void updateSeatData(List<Seat> seats, Booking booking){
        for(Seat seat : seats){
            seat.setBooking(booking);
            seat.setStatus(SeatStatus.RESERVED);
            seatRepository.save(seat);
        }
    }

}