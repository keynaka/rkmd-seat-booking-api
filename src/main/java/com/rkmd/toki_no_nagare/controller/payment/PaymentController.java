package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.entities.payment.Payment;
import com.rkmd.toki_no_nagare.entities.payment.PaymentStatus;
import com.rkmd.toki_no_nagare.exception.UnAuthorizedException;
import com.rkmd.toki_no_nagare.service.AuthorizationService;
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
  private PaymentService paymentService;

  @Autowired
  private AbstractMailingService mailingService;

  @Autowired
  private AuthorizationService authorizationService;

  @PutMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public ChangePaymentResponseDto changePaymentStatus(ChangePaymentRequestDto request, @RequestHeader("x-auth-username") String userName, @RequestHeader("x-auth-password") String password) {
    if (!authorizationService.validatePassword(userName, password))
      throw new UnAuthorizedException("invalid_password", "The password is invalid");

    Payment payment = paymentService.getPaymentByBookingCode(request.getBookingCode());

    ChangePaymentResponseDto response = paymentService.changePaymentStatus(payment, request.getPaymentStatus(), userName);

    if (response.getPayment().getPaymentStatus().equals(PaymentStatus.PAID)) {
      mailingService.notifyConfirmation(payment.getBooking().getClient().getEmail(),
              payment.getBooking().getClient().getName(), payment.getBooking().getClient().getLastName(),
              payment.getBooking().getHashedBookingCode(), payment.getPaymentMethod(),
              payment.getExpirationDate(), Tools.convertSeatToSeatDto(payment.getBooking().getSeats()));
    }

    return response;
  }

  @GetMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public PaymentResponseDto getAllPayment() {
    return paymentService.getAllPayments();
  }
}
