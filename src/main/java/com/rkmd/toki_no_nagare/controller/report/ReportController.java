package com.rkmd.toki_no_nagare.controller.report;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
public class ReportController implements ReportControllerResources{

    @Autowired
    private BookingService bookingService;

    @GetMapping("/v1/reports/bookings/{code_id}")
    public ResponseEntity<BookingResponseDto> getBookingReport(@PathVariable("code_id") String codeId) {
        BookingResponseDto booking = bookingService.getBookingByCode(codeId);

        return ResponseEntity.ok().body(booking);
    }


    @GetMapping(value = "/v1/reports/bookings/{bookingCode}/contacts/{dni}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public BookingResponseDto getBookingByCodeAndDni(String bookingCode, Long dni) {
        return bookingService.getBookingByCodeAndDni(bookingCode, dni);
    }
}
