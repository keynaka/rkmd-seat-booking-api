package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.entities.booking.Booking;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.exception.UnAuthorizedException;
import com.rkmd.toki_no_nagare.service.AuthorizationService;
import com.rkmd.toki_no_nagare.service.BookingService;
import com.rkmd.toki_no_nagare.service.PaymentService;
import com.rkmd.toki_no_nagare.service.mailing.AbstractMailingService;
import com.rkmd.toki_no_nagare.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class PaymentController implements PaymentControllerResources{

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private BookingService bookingService;

  @Autowired
  private AbstractMailingService mailingService;

  @Autowired
  private PaymentService paymentService;



  @PutMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public ChangePaymentResponseDto changePaymentStatus(ChangePaymentRequestDto request, @RequestHeader("x-auth-username") String userName, @RequestHeader("x-auth-password") String password) {
    if (!authorizationService.validatePassword(userName, password))
      throw new UnAuthorizedException("invalid_password", "The password is invalid");

    ChangePaymentResponseDto response = paymentService.changePaymentStatus(request.getBookingCode(), request.getPaymentStatus(), userName);
    Booking booking = bookingService.getBookingByBookingCode(response.getBookingCode());

    if(booking !=null){
      // Step 6: notify confirmation by sending an e-mail to the client
      if (booking.getPayment().getPaymentStatus().equals(PaymentStatus.PAID)) {
        mailingService.notifyConfirmation(
            booking.getClient().getEmail(),
            booking.getClient().getName(),
            booking.getClient().getLastName(),
            booking.getHashedBookingCode(),
            booking.getPayment().getPaymentMethod(),
            booking.getPayment().getExpirationDate(),
            Tools.convertSeatToSeatDto(booking.getSeats()));
      }

      // Step 7: notify expiration by sending an e-mail to the client
      if (booking.getPayment().getPaymentStatus().equals(PaymentStatus.EXPIRED)) {
        mailingService.notifyExpiration(
            booking.getClient().getEmail(),
            booking.getClient().getName(),
            booking.getClient().getLastName(),
            booking.getHashedBookingCode(),
            booking.getPayment().getExpirationDate());
      }

      // Step 8: Notify reservation by sending an e-mail to the backend for backup
      mailingService.notifyReservationBackUp(booking.getHashedBookingCode(), booking.toString());
    }

    return response;
  }

  @GetMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public PaymentResponseDto getAllPayment() {
    return paymentService.getAllPayments();
  }
}
