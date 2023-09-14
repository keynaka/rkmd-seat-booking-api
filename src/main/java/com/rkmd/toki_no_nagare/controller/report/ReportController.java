package com.rkmd.toki_no_nagare.controller.report;

import com.rkmd.toki_no_nagare.dto.booking.BookingListResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.report.BookingStatisticsDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.service.BookingService;
import com.rkmd.toki_no_nagare.service.ReportService;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("")
public class ReportController implements ReportControllerResources{
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ReportService reportService;

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

    @GetMapping("/v1/reports/bookings")
    public ResponseEntity<List<BookingListResponseDto>> getBookingList() {
        List<Booking> bookings = bookingService.getAllOrderedByDateCreated();

        List<BookingListResponseDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingListResponseDto bookingDto = new BookingListResponseDto();
            bookingDto.setBookingCode(booking.getHashedBookingCode());
            bookingDto.setDni(booking.getClient().getDni());
            bookingDto.setTitle(reportService.formatTitle(booking));
            bookingDto.setStatus(reportService.calculateStatus(booking));
            bookingDto.setPaymentMethod(booking.getPayment().getPaymentMethod().name());
            bookingDto.setDateCreated(Tools.changeToArgentinianZonedId(booking.getDateCreated()));
            bookingDto.setLastUpdated(Tools.changeToArgentinianZonedId(booking.getLastUpdated()));
            bookingDto.setSeller(booking.getSeller());

            result.add(bookingDto);
        }

        return ResponseEntity.ok().body(result);
    }


  @GetMapping(value = "/v1/reports/booking/statistics", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public BookingStatisticsDto getGeneralStatus() {
    return bookingService.getGeneralStatus();
  }
}
