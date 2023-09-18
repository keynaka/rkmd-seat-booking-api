package com.rkmd.toki_no_nagare.controller.booking;

import com.rkmd.toki_no_nagare.dto.booking.BookingResponseDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingRequestDto;
import com.rkmd.toki_no_nagare.dto.booking.CreateBookingResponseDto;
import com.rkmd.toki_no_nagare.dto.payment.ChangePaymentResponseDto;
import com.rkmd.toki_no_nagare.dto.seat.SeatRequestDto;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.booking.BookingStatus;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.entities.seat.Seat;
import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.UnAuthorizedException;
import com.rkmd.toki_no_nagare.service.*;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class BookingController implements BookingControllerResources{
    private static String VIP_ADMIN = "RKMD.MATI";
    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private AuthorizationService authorizationService;

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

    @PostMapping(value = "/v2/vip/bookings", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ChangePaymentResponseDto createBookingVip(CreateBookingRequestDto request, @RequestHeader("x-auth-username") String userName, @RequestHeader("x-auth-password") String password) {
        // Step 1: Authorized user
        if (!authorizationService.validatePassword(userName, password))
            throw new UnAuthorizedException("invalid_password", "The password is invalid");

        // Step 2: Set seat price to 0
        if (request.getSeats().size() != 1) throw new BadRequestException("invalid_seat_size", "This booking can only have 1 seat");
        SeatRequestDto requestSeat = request.getSeats().get(0);
        Optional<Seat> optionalSeat = seatService.getSeat(requestSeat.getRow(), requestSeat.getColumn(), requestSeat.getSector());
        if (!optionalSeat.isPresent()) throw new BadRequestException("invalid_seat", "Seat does not exist");

        Seat seat = optionalSeat.get();

        seatService.validateIsNotPrereserved(List.of(seat));

        seatService.setSeatPrice(seat, 0L);

        // Step 3: Create VIP booking for Matias Asato
        CreateBookingResponseDto newBooking = bookingService.createBooking(request);

        // Step 4: Change it to paid
        ChangePaymentResponseDto response = paymentService.changePaymentStatus(newBooking.getBookingCode(), PaymentStatus.PAID, VIP_ADMIN);
        Booking booking = bookingService.getBookingByBookingCode(response.getBookingCode());
        if(booking !=null) {
            // Step 5: notify confirmation by sending an e-mail to Matias Asato
            mailingService.notifyConfirmation(
                    booking.getClient().getEmail(),
                    booking.getClient().getName(),
                    booking.getClient().getLastName(),
                    booking.getHashedBookingCode(),
                    booking.getPayment().getPaymentMethod(),
                    booking.getPayment().getExpirationDate(),
                    Tools.convertSeatToSeatDto(booking.getSeats()));

            // Step 6: Notify reservation by sending an e-mail to the backend for backup
            mailingService.notifyReservationBackUp("(BACKUP) Payment - Code: " + booking.getHashedBookingCode(), booking.toString());
        }


        return response;
    }
}
