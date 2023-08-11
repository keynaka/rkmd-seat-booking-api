package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.entities.seat.SeatId;
import com.rkmd.toki_no_nagare.entities.seat.SeatSector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
    List<Seat> findAllBySector(SeatSector sector);
    List<Seat> findAllBySectorAndRow(SeatSector sector, Long row);
}
