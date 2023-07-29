package com.rkmd.toki_no_nagare.controller;

import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/{booking_id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("booking_id") Long id) throws Exception {
        Optional<Booking> booking = bookingService.get(id);
        if (!booking.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(booking.get());
    }

    @PostMapping("")
    public ResponseEntity<Booking> save(@RequestBody @Valid Map<String, Object> json) {
        Booking newBooking = bookingService.save(json);

        return ResponseEntity.ok().body(newBooking);
    }
}
