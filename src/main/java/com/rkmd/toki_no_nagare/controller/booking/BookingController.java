package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.service.AdminAvailableDateService;
import com.rkmd.toki_no_nagare.service.BookingService;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class BookingController implements BookingControllerResources{
    @Autowired
    private BookingService bookingService;

    @Autowired
    private AbstractMailingService mailingService;

    @Autowired
    private AdminAvailableDateService adminAvailableDateService;

    @PostMapping(value = "/v2/bookings", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateBookingResponseDto createBooking(CreateBookingRequestDto request) {
        CreateBookingResponseDto response = bookingService.createBooking(request);
        Booking booking = bookingService.getBookingByBookingCode(response.getBookingCode());

        if(booking != null){
            // Step 9: Notify reservation by sending an e-mail to the client
            mailingService.notifyReservation(
                booking.getClient().getEmail(),
                booking.getClient().getName(),
                booking.getClient().getLastName(),
                booking.getHashedBookingCode(),
                booking.getPayment().getPaymentMethod(),
                booking.getExpirationDate(),
                Tools.convertSeatToSeatDto(booking.getSeats()),
                adminAvailableDateService.getAvailableDatesForMail(booking.getPayment().getExpirationDate()));

            // Step 10: Notify reservation by sending an e-mail to the backend for backup
            mailingService.notifyReservationBackUp("(BACKUP) Booking - Code: " + booking.getHashedBookingCode(), booking.toString());
        }

        return response;
    }

    @GetMapping("/v3/bookings/{code_id}")
    public ResponseEntity<BookingStatus> getBookingStatus(@PathVariable("code_id") String codeId) {
        BookingResponseDto booking = bookingService.getBookingByCode(codeId);

        return ResponseEntity.ok().body(booking.getStatus());
    }
}
