package com.rkmd.toki_no_nagare.service;

import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public Optional<Booking> get(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Map<String, Object> json) {
        Booking newBooking = new Booking();

        //TODO: CONTINUE HERE...
        /*newBooking.setName((String) json.get("name"));
        newBooking.setLastName((String) json.get("last_name"));
        newBooking.setPasswordHash((String) json.get("password"));*/

        try {
            return bookingRepository.save(newBooking);
        } catch (Exception e) {
            throw new BadRequestException("bad_request", e.getMessage());
        }
    }
}
