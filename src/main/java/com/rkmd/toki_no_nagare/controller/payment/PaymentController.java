package com.rkmd.toki_no_nagare.controller.payment;

import com.rkmd.toki_no_nagare.dto.payment.*;
import com.rkmd.toki_no_nagare.exception.UnAuthorizedException;
import com.rkmd.toki_no_nagare.service.AuthorizationService;
import com.rkmd.toki_no_nagare.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class PaymentController implements PaymentControllerResources{

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private AuthorizationService authorizationService;

  @PutMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public ChangePaymentResponseDto changePaymentStatus(ChangePaymentRequestDto request,
                                                      @RequestHeader("x-auth-username") String userName,
                                                      @RequestHeader("x-auth-password") String password) {
    if (!authorizationService.validatePassword(userName, password))
      throw new UnAuthorizedException("invalid_password", "The password is invalid");

    return paymentService.changePaymentStatus(request.getBookingCode(), request.getPaymentStatus(), userName);
  }

  @GetMapping(value = "/v1/payments", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public PaymentResponseDto getAllPayment() {
    return paymentService.getAllPayments();
  }
}
