package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.service.BookingService;
import com.rkmd.toki_no_nagare.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class BookingController implements BookingControllerResources{
    @Autowired
    private BookingService bookingService;

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

    @PutMapping("/v1/bookings/{code_id}")
    public ResponseEntity<BookingResponseDto> updateBookingStatus(@PathVariable("code_id") String codeId,
                                                                  @RequestParam(name = "status") String status) {
        ValidationUtils.checkParam(
                Arrays.stream(BookingStatus.values()).map(bookingStatus -> bookingStatus.name()).collect(Collectors.toList()).contains(status.toUpperCase()),
                "invalid_booking_status", "The booking status not exists");
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());

        BookingResponseDto updatedBooking = bookingService.updateBooking(codeId, newStatus);

        return ResponseEntity.ok().body(updatedBooking);
    }
}
