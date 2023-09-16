package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByOrderByDateCreatedDesc();

    Optional<Booking> findByHashedBookingCode(String bookingCode);

}
