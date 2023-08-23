package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("")
public class BookingController implements BookingControllerResources{

    @Autowired
    private BookingService bookingService;

    @GetMapping("/v1/booking/{booking_id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("booking_id") Long id) throws Exception {
        Optional<Booking> booking = bookingService.get(id);
        if (!booking.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(booking.get());
    }

    @PostMapping("/v1/booking")
    public ResponseEntity<Booking> save(@RequestBody @Valid Map<String, Object> json) {
        Booking newBooking = bookingService.save(json);

        return ResponseEntity.ok().body(newBooking);
    }

    @GetMapping(value = "/v2/bookings/{bookingCode}/contacts/{dni}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public BookingResponseDto getBookingByCodeAndDni(String bookingCode, Long dni) {
        return bookingService.getBookingByCodeAndDni(bookingCode, dni);
    }

    @PostMapping(value = "/v2/bookings", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto request) {
        return bookingService.createBooking(request);
    }

}
