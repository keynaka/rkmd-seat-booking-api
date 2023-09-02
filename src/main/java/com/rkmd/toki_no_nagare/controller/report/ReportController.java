package com.rkmd.toki_no_nagare.controller.report;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("")
public class ReportController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/v1/reports/bookings/{code_id}")
    public ResponseEntity<BookingResponseDto> getBookingReport(@PathVariable("code_id") String codeId) {
        BookingResponseDto booking = bookingService.getBookingByCode(codeId);

        return ResponseEntity.ok().body(booking);
    }
}
