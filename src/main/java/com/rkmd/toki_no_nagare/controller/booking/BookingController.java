package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class BookingController implements BookingControllerResources{
    @Autowired
    private BookingService bookingService;

    @PostMapping(value = "/v2/bookings", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/v3/bookings/{code_id}")
    public ResponseEntity<BookingStatus> getBookingStatus(@PathVariable("code_id") String codeId) {
        BookingResponseDto booking = bookingService.getBookingByCode(codeId);

        return ResponseEntity.ok().body(booking.getStatus());
    }
}
